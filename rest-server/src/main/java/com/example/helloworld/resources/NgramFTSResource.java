package com.example.helloworld.resources;

import com.example.helloworld.factory.NgramFTSProvider;
import com.example.helloworld.factory.NgramProvider;
import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api/ngramfts")
@Produces(MediaType.APPLICATION_JSON)
public class NgramFTSResource {

	private final NgramFTSProvider ngramProvider;

	public NgramFTSResource() {
		this.ngramProvider = new NgramFTSProvider();
	}
	
	@GET
	@Path("{ngram}")
    @Timed
	public NgramResponse sayHello(@PathParam("ngram") String ngramName,
			@QueryParam("name") Optional<String> name) {
		return ngramProvider.generateNgramResponse(ngramName);
	}

}
