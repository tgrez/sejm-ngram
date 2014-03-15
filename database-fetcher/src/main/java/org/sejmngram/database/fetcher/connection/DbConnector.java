package org.sejmngram.database.fetcher.connection;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

public interface DbConnector {

	void connect();
	NgramResponse retrieve(String ngram, Date from, Date to, int partyId) throws UnsupportedEncodingException;
	void disconnect();
	void readIdFiles(String partyFilename, String poselFilename);
		
}
