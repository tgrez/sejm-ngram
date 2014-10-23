package org.sejmngram.server;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.sejmngram.server.cache.RedisHitCounter;
import org.sejmngram.server.health.DatabaseHealthCheck;
import org.sejmngram.server.resources.NgramFTSResource;
import org.sejmngram.server.resources.NgramHitCountResource;
import org.skife.jdbi.v2.DBI;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;
import com.yammer.dropwizard.jdbi.bundles.DBIExceptionsBundle;

public class RestApiService extends Service<RestApiConfiguration> {
    public static void main(String[] args) throws Exception {
        new RestApiService().run(args);
    }

    @Override
    public void initialize(Bootstrap<RestApiConfiguration> bootstrap) {
        bootstrap.setName("sejm-ngram");
        
        // static assets
        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
        
        // unwrap any thrown SQLException or DBIException instances
        // necessary for getting full stack trace in logs
        bootstrap.addBundle(new DBIExceptionsBundle());
    }

    @Override
    public void run(RestApiConfiguration config,
                    Environment environment) throws ClassNotFoundException {
        final DBI jdbi = new DBIFactory().build(environment, 
        		config.getDatabaseConfiguration(), "mysql");
        environment.addHealthCheck(new DatabaseHealthCheck(jdbi, 15));
        
        RedisHitCounter redisHitCounter = createRedisCounter(config);
        
        environment.addResource(new NgramFTSResource(
        		jdbi,
                redisHitCounter,
        		config.getPartiaIdFilename(),
        		config.getPoselIdFilename()));

        environment.addResource(new NgramHitCountResource(redisHitCounter));


        environment.addFilter(CrossOriginFilter.class, "/*")
                .setInitParam("allowedOrigins", "*")
                .setInitParam("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin")
                .setInitParam("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
    }

	private RedisHitCounter createRedisCounter(RestApiConfiguration config) {
		String redisAddress = config.getRedisAddress();
		if (redisAddress != null) {
			return new RedisHitCounter(redisAddress);
		} else {
			return null;
		}
	}
}
