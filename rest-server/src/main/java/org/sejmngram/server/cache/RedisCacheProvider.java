package org.sejmngram.server.cache;

import java.io.IOException;

import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.google.common.base.Optional;

public class RedisCacheProvider implements CacheProvider<NgramResponse>{

	private static final Logger LOG = LoggerFactory.getLogger(RedisCacheProvider.class);
	
	private static final String CACHE_KEY_PREFIX = "ngrams:cache:";
	private static final int EXPIRE_TIME_SECONDS = 3 * 24 * 60 * 60; // 3 days
	
	private final Jedis jedis;
	private final String maxmemoryPolicy = "volatile-lru";
	private final String maxmemorySamples = "5";
	
	public RedisCacheProvider(String hostname, String maxmemory) {
		this.jedis = new Jedis(hostname);
		this.jedis.configSet("maxmemory", maxmemory);
		this.jedis.configSet("maxmemory-policy", maxmemoryPolicy);
		this.jedis.configSet("maxmemory-samples", maxmemorySamples);
	}

	@Override
	public void store(String key, NgramResponse value) {
		try {
			jedis.setex(CACHE_KEY_PREFIX + key, EXPIRE_TIME_SECONDS,
					JsonProcessor.transformToJson(value));
		} catch (IOException e) {
			LOG.error("Exception was thrown when inserting ngram: " + key + " to cache. ", e);
		}
	}

	@Override
	public Optional<NgramResponse> tryGet(String key) {
		String value = jedis.get(CACHE_KEY_PREFIX + key);
		if (value == null) {
			return Optional.absent();
		}
		NgramResponse response = null;
		try {
			response = JsonProcessor.transform(value, NgramResponse.class);
		} catch (IOException e) {
			LOG.error("Exception was thrown when generating json for ngram: " + key + " from: " + value, e);
		}
		return Optional.fromNullable(response);
	}
}
