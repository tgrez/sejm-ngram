package org.sejmngram.server.resources.impl;

import io.dropwizard.jersey.params.IntParam;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sejmngram.server.cache.HitCounter;
import org.sejmngram.server.resources.HitCountResource;

import com.google.common.base.Optional;

@Path("/api/hitcount")
@Produces(MediaType.APPLICATION_JSON)
public class NgramHitCountResource implements HitCountResource {

    private final Optional<? extends HitCounter> counter;

    public NgramHitCountResource(Optional<? extends HitCounter> counter) {
        this.counter = counter;
    }

    public Set<String> getTopNgram(IntParam limit) {
        if (counter.isPresent() && limit.get() != null) {
            return counter.get().getTop(limit.get());
        } else {
            return new HashSet<String>();
        }
    }
}
