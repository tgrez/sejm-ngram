package com.example.helloworld.resources;

import com.example.helloworld.factory.NgramProvider;
import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api2/ngram")
@Produces(MediaType.APPLICATION_JSON)
public class DemoNgramResource {

	private final NgramProvider ngramProvider;

	public DemoNgramResource() {
		this.ngramProvider = new NgramProvider();
	}
	
	@GET
	@Path("{ngram}")
    @Timed
	public NgramResponse sayHello(@PathParam("ngram") String ngramName,
			@QueryParam("name") Optional<String> name) {
		return ngramProvider.generateNgramResponse(ngramName);
	}

}
