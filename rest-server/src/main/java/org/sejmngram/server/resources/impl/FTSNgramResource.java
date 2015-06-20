package org.sejmngram.server.resources.impl;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.connection.MySqlFtsDbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.server.resources.NgramResource;
import org.skife.jdbi.v2.DBI;

@Path("/api/ngramfts")
@Produces(MediaType.APPLICATION_JSON)
public class FTSNgramResource implements NgramResource {

    private final DbConnector db;

    public FTSNgramResource(DBI jdbi, String partyFilename,
            String poselFilename, String datesFilename) {
        this.db = new MySqlFtsDbConnector(jdbi, partyFilename, poselFilename, datesFilename);
        this.db.connect();
    }

    public NgramResponse getNgram(String ngramName) {
        return db.retrieve(ngramName);
    }

}
