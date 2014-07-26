package org.sejmngram.database.fetcher.connection;

import org.sejmngram.database.fetcher.json.datamodel.NgramOccurencesResponse;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

import java.util.Date;

public interface DbConnector {

	void connect();
	void disconnect();
	
	NgramResponse retrieve(String ngram);
	NgramResponse retrieveByParty(String ngram, int partyId);
	NgramResponse retrieveByPosel(String ngram, int poselId);

    NgramOccurencesResponse retrieveNgramOccurences(String ngram, Date date, int limitPerPage, int nrPageRequested);



}
