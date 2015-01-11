package org.sejmngram.server.resources;

import io.dropwizard.jersey.params.IntParam;

import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.codahale.metrics.annotation.Timed;

public interface HitCountResource {

    @GET
    @Path("/top")
    @Timed
    Set<String> getTopNgram(@QueryParam("limit") @DefaultValue("10") IntParam limit);
}
