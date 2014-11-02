package org.sejmngram.server.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.connection.MySqlFtsDbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.server.cache.CacheProvider;
import org.sejmngram.server.cache.HitCounter;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/api/ngramfts")
@Produces(MediaType.APPLICATION_JSON)
public class NgramFTSResource {

    private final DbConnector db;
    private final Optional<? extends HitCounter> counter;
    private final Optional<? extends CacheProvider<NgramResponse>> cacheProvider;

    private static final Logger LOG = LoggerFactory
            .getLogger(NgramFTSResource.class);

    public NgramFTSResource(DBI jdbi, Optional<? extends HitCounter> counter,
            Optional<? extends CacheProvider<NgramResponse>> cacheProvider, String partyFilename,
            String poselFilename) {
        this.counter = counter;
        this.cacheProvider = cacheProvider;
        this.db = new MySqlFtsDbConnector(jdbi, partyFilename, poselFilename);
        this.db.connect();
    }

    @GET
    @Path("{ngram}")
    @Timed
    public NgramResponse getNgram(@PathParam("ngram") String ngramName) {
        incrementHitCount(ngramName);
        LOG.debug("received request");
        if (cacheProvider.isPresent()) {
            Optional<NgramResponse> cachedResponse = cacheProvider.get()
                    .tryGet(ngramName);
            if (cachedResponse.isPresent()) {
                LOG.debug("retrieved from cache");
                return cachedResponse.get();
            }
        }
        NgramResponse dbResponse = db.retrieve(ngramName);
        LOG.debug("retrieved from db");
        if (cacheProvider.isPresent()) {
            cacheProvider.get().store(ngramName, dbResponse);
            LOG.debug("stored in cache");
        }
        return dbResponse;
    }

    private void incrementHitCount(String ngramName) {
        if (counter.isPresent()) {
            counter.get().increment(ngramName);
        }
    }

}
