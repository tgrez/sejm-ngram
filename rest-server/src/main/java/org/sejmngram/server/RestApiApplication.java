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
import org.sejmngram.server.resources.NgramFTSResource;
import org.sejmngram.server.resources.NgramHitCountResource;
import org.skife.jdbi.v2.DBI;

import com.google.common.base.Optional;

public class RestApiApplication extends Application<RestApiConfiguration> {

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

        DBI jdbi = new DBIFactory().build(environment,
                config.getDataSourceFactory(), "mysql");

        RedisFactory redisFactory = new RedisFactory();
        Optional<RedisConnection> redisConnection = redisFactory.createRedisConnection(config.getRedis());

        if (redisConnection.isPresent()) {
            environment.lifecycle().manage(redisConnection.get());
            environment.healthChecks().register("redis", new RedisHealthCheck(redisConnection.get()));
        }
        int dbHealthCheckTimeout = 15;
        environment.healthChecks().register("database-jdbi", new DatabaseHealthCheck(jdbi, dbHealthCheckTimeout));

        environment.jersey().register(new NgramFTSResource(
                jdbi,
                redisFactory.createRedisCounter(redisConnection),
                redisFactory.createRedisCacheProvider(redisConnection),
                config.getPartiaIdFilename(),
                config.getPoselIdFilename()));

        environment.jersey().register(new NgramHitCountResource(redisFactory.createRedisCounter(redisConnection)));

        //add filters for cors
        Dynamic filter = environment.servlets()
                .addFilter("crossOriginFilter", CrossOriginFilter.class);
        filter.setInitParameter("allowedOrigins", "*");
        filter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        filter.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
    }
}
