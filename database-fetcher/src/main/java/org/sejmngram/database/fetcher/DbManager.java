package org.sejmngram.database.fetcher;

import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.connection.DbConnectorMock;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

public class DbManager {

	private DbConnector dbConn = new DbConnectorMock();
	
	public static class A {
		
	}
	
	public NgramResponse filterByParty(String ngram, int partyId) {
		return dbConn.retrieve(ngram, null, null, partyId);
	}
}
