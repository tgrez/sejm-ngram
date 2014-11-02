package org.sejmngram.server.redis;

import io.dropwizard.lifecycle.Managed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisConnection extends JedisPool implements Managed {

    private static final Logger LOG = LoggerFactory
            .getLogger(RedisConnection.class);

    private final String maxMemory;
    private final String maxMemoryPolicy;
    private final int maxMemorySamples;

    RedisConnection(RedisConfiguration config) {
        super(new JedisPoolConfig(), config.getHost(), config.getPort());
        this.maxMemory = config.getMaxMemory();
        this.maxMemoryPolicy = config.getMaxMemoryPolicy();
        this.maxMemorySamples = config.getMaxMemorySamples();
    }

    @Override
    public void start() throws Exception {
        try (Jedis jedis = getResource()) {
            jedis.configSet("maxmemory", maxMemory);
            jedis.configSet("maxmemory-policy", maxMemoryPolicy);
            jedis.configSet("maxmemory-samples", Integer.toString(maxMemorySamples));
        }
        LOG.info("Successfully started redis connection.");
    }

    @Override
    public void stop() throws Exception {
        LOG.debug("Stopping redis connection.");
        this.destroy();
    }

}
