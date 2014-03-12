package org.sejmngram.database.fetcher.connection;

import java.util.Date;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

public interface DbConnector {

	void connect();
	NgramResponse retrieve(String ngram, Date from, Date to, int partyId);
		
}
