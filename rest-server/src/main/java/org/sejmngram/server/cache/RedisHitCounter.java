package org.sejmngram.server.cache;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisHitCounter implements HitCounter {

    private static final String HIT_COUNT_KEY_NAME = "ngrams:hitcount";

    private JedisPool jedisPool;

    public RedisHitCounter(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void increment(String ngramName) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.zincrby(HIT_COUNT_KEY_NAME, 1, ngramName);
        }
    }

    @Override
    public Set<String> getTop(int limit) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrevrange(HIT_COUNT_KEY_NAME, 0, limit - 1);
        }
        // Set<Tuple> result2 = jedis.zrevrangeWithScores(HIT_COUNT_KEY_NAME, 0,
        // limit - 1);
    }

}
