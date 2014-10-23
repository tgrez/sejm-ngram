package org.sejmngram.server.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.server.cache.CacheProvider;
import org.sejmngram.server.cache.HitCounter;
import org.sejmngram.server.factory.NgramFTSProvider;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;

@Path("/api/ngramfts")
@Produces(MediaType.APPLICATION_JSON)
public class NgramFTSResource {

	private final NgramFTSProvider ngramProvider;
	private final HitCounter counter;
	private final CacheProvider<NgramResponse> cacheProvider;

	private static final Logger LOG = LoggerFactory.getLogger(NgramFTSResource.class);

	public NgramFTSResource(DBI jdbi, HitCounter counter, CacheProvider<NgramResponse> cacheProvider,
			String partyFilename, String poselFilename) {
		this.ngramProvider = new NgramFTSProvider(jdbi, partyFilename, poselFilename);
		this.counter = counter;
		this.cacheProvider = cacheProvider;
	}
	
	@GET
	@Path("{ngram}")
    @Timed
	public NgramResponse getNgram(@PathParam("ngram") String ngramName) {
		incrementHitCount(ngramName);
		LOG.debug("received request");
		if (cacheProvider != null) {
			Optional<NgramResponse> cachedResponse = cacheProvider.tryGet(ngramName);
			if (cachedResponse.isPresent()) {
				LOG.debug("retrieved from cache");
				return cachedResponse.get();
			}
		}
		NgramResponse dbResponse = ngramProvider.generateNgramResponse(ngramName);
		LOG.debug("retrieved from db");
		if (cacheProvider != null) {
			cacheProvider.store(ngramName, dbResponse);
			LOG.debug("stored in cache");
		}
		return dbResponse;
	}

	private void incrementHitCount(String ngramName) {
		if (counter != null) {
			counter.increment(ngramName);
		}
	}

}
