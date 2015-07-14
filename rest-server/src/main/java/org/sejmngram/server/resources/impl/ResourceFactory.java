package org.sejmngram.server.resources.impl;

import io.dropwizard.elasticsearch.config.EsConfiguration;
import io.dropwizard.elasticsearch.managed.ManagedEsClient;

import org.sejmngram.server.cache.HitCounter;
import org.sejmngram.server.config.SejmFilesConfiguration;
import org.sejmngram.server.redis.RedisConnection;
import org.sejmngram.server.redis.RedisFactory;
import org.sejmngram.server.resources.HitCountResource;
import org.skife.jdbi.v2.DBI;

import com.google.common.base.Optional;

public class ResourceFactory {

    private RedisFactory redisFactory;

    public ResourceFactory(RedisFactory redisFactory) {
        this.redisFactory = redisFactory;
    }

    public FTSNgramResource createFTSNgramResource(DBI jdbi, Optional<RedisConnection> redisConnection,
            SejmFilesConfiguration sejmFilesConfig) {
        if (redisConnection.isPresent()) {
            return new CachedFTSNgramResource(jdbi,
                    redisFactory.createRedisCounter(redisConnection.get()),
                    redisFactory.createRedisCacheProvider(redisConnection.get()),
                    sejmFilesConfig);
        } else {
            return new FTSNgramResource(jdbi, sejmFilesConfig);
        }
    }

    public ElasticSearchNgramResource createElasticSearchNgramResource(Optional<RedisConnection> redisConnection,
            ManagedEsClient managedEsClient,
            EsConfiguration esConfig,
            SejmFilesConfiguration sejmFilesConfig) {
        return new ElasticSearchNgramResource(managedEsClient, esConfig, sejmFilesConfig);
    }

    public HitCountResource createHitCountResource(Optional<RedisConnection> redisConnection) {
        if (redisConnection.isPresent()) {
            return new NgramHitCountResource(
                    Optional.of(redisFactory.createRedisCounter(redisConnection.get())));
        } else {
            return new NgramHitCountResource(Optional.<HitCounter>absent()); 
        }
    }
}
