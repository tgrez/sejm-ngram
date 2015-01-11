package org.sejmngram.server.redis;

import org.sejmngram.server.cache.RedisCacheProvider;
import org.sejmngram.server.cache.RedisHitCounter;

import com.google.common.base.Optional;

public class RedisFactory {

    public Optional<RedisConnection> createRedisConnection(RedisConfiguration config) {
        if (config != null && config.getHost() != null && !config.getHost().isEmpty()) {
            return Optional.of(new RedisConnection(config));
        } else {
            return Optional.absent();
        }
    }

    public RedisCacheProvider createRedisCacheProvider(RedisConnection redisConnection) {
        return new RedisCacheProvider(redisConnection);
    }

    public RedisHitCounter createRedisCounter(RedisConnection redisConnection) {
        return new RedisHitCounter(redisConnection);
    }
}
