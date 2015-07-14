package org.sejmngram.server.resources.impl;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.server.cache.CacheProvider;
import org.sejmngram.server.cache.HitCounter;
import org.sejmngram.server.config.SejmFilesConfiguration;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

@Path("/api/ngramfts")
@Produces(MediaType.APPLICATION_JSON)
public class CachedFTSNgramResource extends FTSNgramResource {

    private static final Logger LOG = LoggerFactory.getLogger(CachedFTSNgramResource.class);

    private final HitCounter counter;
    private final CacheProvider<NgramResponse> cacheProvider;

    public CachedFTSNgramResource(DBI jdbi, HitCounter counter,
            CacheProvider<NgramResponse> cacheProvider,
            SejmFilesConfiguration sejmFilesConfiguration) {
        super(jdbi, sejmFilesConfiguration);
        this.counter = counter;
        this.cacheProvider = cacheProvider;
    }

    @Override
    public NgramResponse getNgram(String ngramName) {
        LOG.debug("received request: " + ngramName);
        incrementHitCount(ngramName);
        // TODO could be refactored with Java 8 and Optional.orElse() if it increases readability
        Optional<NgramResponse> cachedResponse = cacheProvider.tryGet(ngramName);
        if (cachedResponse.isPresent()) {
            LOG.debug("retrieved from cache");
            return cachedResponse.get();
        }
        NgramResponse dbResponse = super.getNgram(ngramName);
        LOG.debug("retrieved from db");
        cacheProvider.store(ngramName, dbResponse);
        LOG.debug("stored in cache");
        return dbResponse;
    }

    private void incrementHitCount(String ngramName) {
        counter.increment(ngramName);
    }

}
