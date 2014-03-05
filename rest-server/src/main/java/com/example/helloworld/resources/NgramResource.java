package com.example.helloworld.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

import com.example.helloworld.factory.NgramFactory;
import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;

@Path("/api/ngram")
@Produces(MediaType.APPLICATION_JSON)
public class NgramResource {

	private final NgramFactory ngramFactory;
	
	public NgramResource(NgramFactory ngramFactory) {
		this.ngramFactory = ngramFactory;
	}
	
	@GET
	@Path("{ngram}")
    @Timed
	public NgramResponse sayHello(@PathParam("ngram") String ngramName,
			@QueryParam("name") Optional<String> name) {
		return ngramFactory.generateNgramResponse(ngramName);
	}
}
