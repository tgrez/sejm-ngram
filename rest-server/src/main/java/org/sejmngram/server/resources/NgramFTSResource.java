package org.sejmngram.server.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.server.cache.Counter;
import org.sejmngram.server.factory.NgramFTSProvider;
import org.skife.jdbi.v2.DBI;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;

@Path("/api/ngramfts")
@Produces(MediaType.APPLICATION_JSON)
public class NgramFTSResource {

	private final NgramFTSProvider ngramProvider;
	private final Counter counter;

	public NgramFTSResource(DBI jdbi, Counter counter,
			String partyFilename, String poselFilename) {
		this.ngramProvider = new NgramFTSProvider(jdbi, partyFilename, poselFilename);
		this.counter = counter;
	}
	
	@GET
	@Path("{ngram}")
    @Timed
	public NgramResponse getNgram(@PathParam("ngram") String ngramName,
			@QueryParam("name") Optional<String> name) {
		incrementHitCount(ngramName);
		return ngramProvider.generateNgramResponse(ngramName);
	}

	private void incrementHitCount(String ngramName) {
		if (counter != null) {
			counter.increment(ngramName);
		}
	}

}
