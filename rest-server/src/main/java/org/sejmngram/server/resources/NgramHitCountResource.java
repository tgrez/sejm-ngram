package org.sejmngram.server.resources;

import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.sejmngram.server.cache.Counter;

import com.yammer.dropwizard.jersey.params.IntParam;
import com.yammer.metrics.annotation.Timed;

@Path("/api/hitcount")
@Produces(MediaType.APPLICATION_JSON)
public class NgramHitCountResource {

	private final Counter counter;

	public NgramHitCountResource(Counter counter) {
		this.counter = counter;
	}
	
	@GET
	@Path("/top")
	@Timed
	public Set<String> getTopNgram(@QueryParam("limit") @DefaultValue("10") IntParam limit) {
		return counter.getTop(limit.get());
	}
}
