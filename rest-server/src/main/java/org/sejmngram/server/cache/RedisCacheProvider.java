package org.sejmngram.server.cache;

import java.io.IOException;

import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.google.common.base.Optional;

public class RedisCacheProvider implements CacheProvider<NgramResponse> {

    private static final Logger LOG = LoggerFactory
            .getLogger(RedisCacheProvider.class);

    private static final String CACHE_KEY_PREFIX = "ngrams:cache:";
    private static final int EXPIRE_TIME_SECONDS = 3 * 24 * 60 * 60; // 3 days

    private JedisPool jedisPool;

    public RedisCacheProvider(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void store(String key, NgramResponse value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(CACHE_KEY_PREFIX + key, EXPIRE_TIME_SECONDS,
                    JsonProcessor.transformToJson(value));
        } catch (IOException e) {
            LOG.error("Exception was thrown when inserting ngram: " + key
                    + " to cache. ", e);
        }
    }

    @Override
    public Optional<NgramResponse> tryGet(String key) {
        NgramResponse response = null;
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(CACHE_KEY_PREFIX + key);
            if (value == null) {
                return Optional.absent();
            }
            response = JsonProcessor.transform(value, NgramResponse.class);
        } catch (IOException e) {
            LOG.error("Exception was thrown when generating json for ngram: "
                    + key + " exception: ", e);
        }
        return Optional.fromNullable(response);
    }
}
