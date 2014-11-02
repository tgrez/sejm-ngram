package org.sejmngram.server.resources;

import io.dropwizard.jersey.params.IntParam;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.sejmngram.server.cache.HitCounter;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/api/hitcount")
@Produces(MediaType.APPLICATION_JSON)
public class NgramHitCountResource {

    private final Optional<? extends HitCounter> counter;

    public NgramHitCountResource(Optional<? extends HitCounter> counter) {
        this.counter = counter;
    }

    @GET
    @Path("/top")
    @Timed
    public Set<String> getTopNgram(
            @QueryParam("limit") @DefaultValue("10") IntParam limit) {
        if (counter.isPresent() && limit.get() != null) {
            return counter.get().getTop(limit.get());
        } else {
            return new HashSet<String>();
        }
    }
}
