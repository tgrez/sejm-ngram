package org.sejmngram.database.fetcher.connection;

import java.util.Date;
import java.util.List;

import org.sejmngram.database.fetcher.model.Ngram;

public interface DbConnector {

	void connect();
	List<Ngram> retrieve(Date from, Date to);
		
}
