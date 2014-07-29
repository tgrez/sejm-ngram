package org.sejmngram.server.cache;

import java.util.Set;
import java.util.SortedSet;

import redis.clients.jedis.Jedis;

public class RedisProvider implements Counter {

	private static final String HIT_COUNT_KEY_NAME = "ngrams:hitcount";
	
	private final Jedis jedis;
	
	public RedisProvider() {
		this("localhost");
	}
	
	public RedisProvider(String hostname) {
		jedis = new Jedis(hostname);
	}

	@Override
	public void increment(String ngramName) {
		jedis.zincrby(HIT_COUNT_KEY_NAME, 1, ngramName);
	}

	@Override
	public Set<String> getTop(int limit) {
		return jedis.zrevrange(HIT_COUNT_KEY_NAME, 0, limit - 1);
//		Set<Tuple> result2 = jedis.zrevrangeWithScores(HIT_COUNT_KEY_NAME, 0, limit - 1);
	}

}
