package org.sejmngram.database.fetcher.connection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sejmngram.database.fetcher.model.Ngram;

public class DbConnectorMock implements DbConnector {

	private static final String filename = "ngrams_mocking.txt";
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	
	@Override
	public void connect() {
		
	}

	@Override
	public List<Ngram> retrieve(Date from, Date to) {
		List<Ngram> result = new ArrayList<Ngram>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename), 32768);
			String line;
			while((line = br.readLine()) != null) {
			     String[] columns = line.split("|");
			     Ngram ngram = new Ngram();
			     ngram.setId(Integer.valueOf(columns[0]));
			     ngram.setFrom(df.parse(columns[1]));
			     ngram.setTo(df.parse(columns[2]));
			     ngram.setCount(Integer.valueOf(columns[3]));
			     ngram.setBlob(columns[4].getBytes("UTF-8"));
			     result.add(ngram);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return result;
	}
}
