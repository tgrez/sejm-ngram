package org.sejmngram.server.factory;

import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.connection.MySqlFtsDbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.skife.jdbi.v2.DBI;

public class NgramFTSProvider {

    private DbConnector db;

    public NgramFTSProvider(DBI jdbi, String partyFilename, String poselFilename) {
        db = new MySqlFtsDbConnector(jdbi, partyFilename, poselFilename);
        db.connect();
    }

    public NgramResponse generateNgramResponse(String ngramName) {
        return db.retrieve(ngramName);
    }
}
