package com.example.helloworld.factory;

import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.connection.MySqlFtsDbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

public class NgramFTSProvider {

    private DbConnector db;

    public NgramFTSProvider() {
        db = new MySqlFtsDbConnector();
        db.connect();
    }

    public NgramResponse generateNgramResponse(String ngramName) {
        return db.retrieve(ngramName);
    }
}
