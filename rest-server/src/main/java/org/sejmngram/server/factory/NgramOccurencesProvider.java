package org.sejmngram.server.factory;

import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.connection.MySqlFtsDbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramOccurencesResponse;
import org.skife.jdbi.v2.DBI;

import java.util.Date;

public class NgramOccurencesProvider {

    private DbConnector db;

    public NgramOccurencesProvider(DBI jdbi, String partyFilename, String poselFilename) {
        db = new MySqlFtsDbConnector(jdbi, partyFilename, poselFilename);
        db.connect();
    }

    public NgramOccurencesResponse generateNgramOccurenceResponse(String ngramName, Date date,
                                                                  int limitPerPage, int nrPageRequested) {
        return db.retrieveNgramOccurences(ngramName, date, limitPerPage, nrPageRequested);
    }
}
