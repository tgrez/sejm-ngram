package org.sejmngram.server;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.sejmngram.server.health.DatabaseHealthCheck;
import org.sejmngram.server.health.RedisHealthCheck;
import org.sejmngram.server.redis.RedisConnection;
import org.sejmngram.server.redis.RedisFactory;
import org.sejmngram.server.resources.impl.ResourceFactory;
import org.skife.jdbi.v2.DBI;

import com.google.common.base.Optional;

public class RestApiApplication extends Application<RestApiConfiguration> {
    
    private static final int DB_HEALTH_CHECK_TIMEOUT = 15;
    
    private RedisFactory redisFactory = new RedisFactory();
    private ResourceFactory ngramResourceFactory = new ResourceFactory(redisFactory);

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
    public void run(RestApiConfiguration config, Environment environment)
            throws Exception {
        environment.jersey().setUrlPattern("/service/*");

        DBI jdbi = new DBIFactory().build(environment, config.getDataSourceFactory(), "mysql");

        Optional<RedisConnection> redisConnection = redisFactory.createRedisConnection(config.getRedis());
        if (redisConnection.isPresent()) {
            environment.lifecycle().manage(redisConnection.get());
            environment.healthChecks().register("redis", new RedisHealthCheck(redisConnection.get()));
        }
        environment.healthChecks().register("database-jdbi", new DatabaseHealthCheck(jdbi, DB_HEALTH_CHECK_TIMEOUT));

        environment.jersey().register(ngramResourceFactory.createFTSNgramResource(jdbi, redisConnection,
                config.getPartiaIdFilename(), config.getPartiaIdFilename()));
        environment.jersey().register(ngramResourceFactory.createHitCountResource(redisConnection));

        addCrossOriginFilter(environment);
    }

    private void addCrossOriginFilter(Environment environment) {
        //add filters for cors
        Dynamic filter = environment.servlets().addFilter("crossOriginFilter", CrossOriginFilter.class);
        filter.setInitParameter("allowedOrigins", "*");
        filter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        filter.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
    }
}
