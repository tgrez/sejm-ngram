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

    public Optional<RedisCacheProvider> createRedisCacheProvider(Optional<RedisConnection> redisConnection) {
        if (redisConnection.isPresent()) {
            return Optional.of(new RedisCacheProvider(redisConnection.get()));
        } else {
            return Optional.absent();
        }
    }

    public Optional<RedisHitCounter> createRedisCounter(Optional<RedisConnection> redisConnection) {
        if (redisConnection.isPresent()) {
            return Optional.of(new RedisHitCounter(redisConnection.get()));
        } else {
            return Optional.absent();
        }
    }
}
