package org.sejmngram.server;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.elasticsearch.health.EsClusterHealthCheck;
import io.dropwizard.elasticsearch.managed.ManagedEsClient;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.sejmngram.server.config.RestApiConfiguration;
import org.sejmngram.server.health.DatabaseHealthCheck;
import org.sejmngram.server.health.RedisHealthCheck;
import org.sejmngram.server.redis.RedisConnection;
import org.sejmngram.server.redis.RedisFactory;
import org.sejmngram.server.resources.impl.ResourceFactory;
import org.skife.jdbi.v2.DBI;

import com.google.common.base.Optional;

public class RestApiApplication extends Application<RestApiConfiguration> {

    private static final int DB_HEALTH_CHECK_TIMEOUT = 15; // seconds
    
    // TODO introduce dependency injection?
    private RedisFactory redisFactory = new RedisFactory();
    private ResourceFactory ngramResourceFactory = new ResourceFactory(redisFactory);
    private Optional<RedisConnection> redisConnection;
    private DBI jdbi;
    private ManagedEsClient elasticSearchClient;

    public static void main(String[] args) throws Exception {
        new RestApiApplication().run(args);
    }

    @Override
    public String getName() {
        return "sejm-ngram";
    }

    @Override
    public void initialize(Bootstrap<RestApiConfiguration> bootstrap) {
        // static assets
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.htm"));

        // unwrap any thrown SQLException or DBIException instances
        // necessary for getting full stack trace in logs
        bootstrap.addBundle(new DBIExceptionsBundle());
    }

    @Override
    public void run(RestApiConfiguration config, Environment environment) {

        registerDatabase(config, environment);
        registerElasticSearch(config, environment);
        registerRedis(config, environment);
        registerResources(config, environment);

        addCrossOriginFilter(environment);
    }

    private void registerResources(RestApiConfiguration config, Environment environment) {
        environment.jersey().register(ngramResourceFactory.createElasticSearchNgramResource(
                redisConnection, elasticSearchClient, config.getElasticsearch(), config.getSejmFilesConfig()));
        environment.jersey().register(ngramResourceFactory.createHitCountResource(redisConnection));
        // TODO healthchecks
    }

    private void registerRedis(RestApiConfiguration config, Environment environment) {
        redisConnection = redisFactory.createRedisConnection(config.getRedis());
        if (redisConnection.isPresent()) {
            environment.lifecycle().manage(redisConnection.get());
            environment.healthChecks().register("redis", new RedisHealthCheck(redisConnection.get()));
        }
    }

    private void registerElasticSearch(RestApiConfiguration config, Environment environment) {
        if (config.getElasticsearch() != null) {
            elasticSearchClient = new ManagedEsClient(config.getElasticsearch());
            environment.lifecycle().manage(elasticSearchClient);
            environment.healthChecks().register("ElasticSearch cluster health",
                    new EsClusterHealthCheck(elasticSearchClient.getClient()));
        }
    }

    // TODO create separate branch for database
    private void registerDatabase(RestApiConfiguration config, Environment environment) {
        jdbi = new DBIFactory().build(environment, config.getDataSourceFactory(), "mysql");
        environment.healthChecks().register("database-jdbi", new DatabaseHealthCheck(jdbi, DB_HEALTH_CHECK_TIMEOUT));
    }

    private void addCrossOriginFilter(Environment environment) {
        Dynamic filter = environment.servlets().addFilter("crossOriginFilter", CrossOriginFilter.class);
        filter.setInitParameter("allowedOrigins", "*");
        filter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        filter.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
    }
}
