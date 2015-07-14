package org.sejmngram.server.resources.impl;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.connection.MySqlDbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.server.resources.NgramResource;

@Path("/api/ngramblob")
@Produces(MediaType.APPLICATION_JSON)
public class BlobNgramResource implements NgramResource {

    private final DbConnector db;

    public BlobNgramResource() {
        this.db = new MySqlDbConnector();
        this.db.connect();
    }

    public NgramResponse getNgram(String ngramName) {
        return db.retrieve(ngramName);
    }

}
