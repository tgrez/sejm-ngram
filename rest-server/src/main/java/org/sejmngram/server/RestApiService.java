package org.sejmngram.server;

import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.sejmngram.server.cache.RedisCacheProvider;
import org.sejmngram.server.cache.RedisHitCounter;
import org.sejmngram.server.health.DatabaseHealthCheck;
import org.sejmngram.server.resources.NgramFTSResource;
import org.sejmngram.server.resources.NgramHitCountResource;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class RestApiService extends Application<RestApiConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(RestApiService.class);

    public static void main(String[] args) throws Exception {
        new RestApiService().run(args);
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

        int dbHealthCheckTimeout = 15;
        environment.healthChecks().register("database-jdbi", new DatabaseHealthCheck(jdbi, dbHealthCheckTimeout));
//        environment.addHealthCheck(new RedisHealthCheck(redisHostname));
        
        JedisPool jedisPool = createJedisPool(config);
        RedisHitCounter redisHitCounter = createRedisCounter(jedisPool);
        RedisCacheProvider redisCache = createRedisCacheProvider(jedisPool);
        
        environment.jersey().register(new NgramFTSResource(
                jdbi,
                redisHitCounter,
                redisCache,
                config.getPartiaIdFilename(),
                config.getPoselIdFilename()));

        environment.jersey().register(new NgramHitCountResource(redisHitCounter));

        //add filters for cors
        Dynamic filter = environment.servlets()
                .addFilter("crossOriginFilter", CrossOriginFilter.class);
        filter.setInitParameter("allowedOrigins", "*");
        filter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        filter.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
    }

    private JedisPool createJedisPool(RestApiConfiguration config) {
        String redisAddress = config.getRedisAddress();
        if (redisAddress == null) {
            return null;
        }
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), redisAddress);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.configSet("maxmemory", "1000000000"); // in bytes for redis 2.8.13
            jedis.configSet("maxmemory-policy", "volatile-lru");
            jedis.configSet("maxmemory-samples", "5");
        }
        LOG.info("Successfully created jedis pool for redis access.");
        return jedisPool;
    }

    private RedisCacheProvider createRedisCacheProvider(JedisPool jedisPool) {
        if (jedisPool != null) {
            return new RedisCacheProvider(jedisPool);
        } else {
            return null;
        }
    }

    private RedisHitCounter createRedisCounter(JedisPool jedisPool) {
        if (jedisPool != null) {
            return new RedisHitCounter(jedisPool);
        } else {
            return null;
        }
    }
}
