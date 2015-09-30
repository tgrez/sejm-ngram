package org.sejmngram.database.fetcher.connection;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

public interface NgramDbConnector {

    void connect();
    void disconnect();

    NgramResponse retrieve(String ngram);
    NgramResponse retrieveByParty(String ngram, int partyId);
    NgramResponse retrieveByPosel(String ngram, int poselId);
}
