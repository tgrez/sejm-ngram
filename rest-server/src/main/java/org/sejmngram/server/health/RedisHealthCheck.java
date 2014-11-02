package org.sejmngram.server.health;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.codahale.metrics.health.HealthCheck;

public class RedisHealthCheck extends HealthCheck {

    private final JedisPool jedisPool;

    public RedisHealthCheck(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    protected Result check() {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.isConnected()) {
                return Result.healthy();
            } else {
                return Result.unhealthy("Jedis is not connected.");
            }
        }
    }

}
