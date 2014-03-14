package org.sejmngram.database.fetcher;

import java.io.UnsupportedEncodingException;

import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.connection.DbConnectorMock;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

public class DbManager {

	private DbConnector dbConn = new DbConnectorMock();
	
	public static class A {
		
	}
	
	public NgramResponse filterByParty(String ngram, int partyId) throws UnsupportedEncodingException {
		return dbConn.retrieve(ngram, null, null, partyId);
	}
}
