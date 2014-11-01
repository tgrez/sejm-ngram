package org.sejmngram.server.health;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.tweak.HandleCallback;

import com.codahale.metrics.health.HealthCheck;

public class DatabaseHealthCheck extends HealthCheck {

	private DBI jdbi;
	private int timeout;

	public DatabaseHealthCheck(DBI jdbi, int timeout) {
		this.jdbi = jdbi;
		this.timeout = timeout;
	}

	@Override
	protected Result check() throws Exception {
		Boolean result = Boolean.FALSE;
		try {
			result = jdbi.withHandle(new HandleCallback<Boolean>() {
				public Boolean withHandle(Handle handle) throws Exception {
					return handle.getConnection().isValid(timeout);
				}
			});
		} catch (Exception e) {
			return Result.unhealthy("Caught exception: " + e);
		}
		if (result == Boolean.TRUE) {
			return Result.healthy();
		} else {
			return Result.unhealthy("Connection is closed or not valid.");
		}
	}

}
