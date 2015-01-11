package org.sejmngram.server.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

import com.codahale.metrics.annotation.Timed;

public interface NgramResource {

    @GET
    @Path("{ngram}")
    @Timed
    NgramResponse getNgram(@PathParam("ngram") String ngramName);
}
