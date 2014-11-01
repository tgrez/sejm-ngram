package org.sejmngram.server.health;

import redis.clients.jedis.Jedis;

import com.codahale.metrics.health.HealthCheck;

public class RedisHealthCheck extends HealthCheck {

	private Jedis jedis;
	
	public RedisHealthCheck(String hostname) {
		this.jedis = new Jedis(hostname);
	}

	@Override
	protected Result check() throws Exception {
		if (jedis.isConnected()) {
			return Result.healthy();
		} else {
			return Result.unhealthy("Jedis says it is not connected.");
		}
		/*
		 JedisPool pool = new JedisPool(...);
		try {
		    Jedis jedis = pool.getResource();
		    // Is connected
		    // TODO return jedis back to pool
		     * pool.returnResource(jedis);
		     * jedis.set("foo","bar");
  assertEquals("bar",jedis.get("foo"));
		} catch (JedisConnectionException e) {
		    // Not connected
		}
		*/
	}

}
