package org.sejmngram.database.fetcher.converter;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.maven.example.tables.Ngrams;
import org.sejmngram.database.fetcher.json.datamodel.ListDate;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.PartiesNgrams;

public class Converter {
	
	// 1213228800%235%6^1213228800%84%2^1213228800%308%1^1213228800%282%1^1213228800%18
	//%9^1213228800%209%8^1213228800%327%2^1213228800%332%2^1213228800%225%0^1213228800%371%7^1213228800%315%8^1213228800%52%9^1213228800%156%5^1213228800%386%7^1213228800%313%2^1213228800%375%2^1213228800%261%6^1213228800%191%7^1213228800%182%7^1213228800%206%8^1213228800%230%2^1213228800%288%3^1213228800%105%2^1213228800%18%6^1213228800%271%3^1213228800%174%0^1213228800%349%0^1213228800%151%8^1213228800%265%9^1213228800%387%3^847411200%59%4^847411200%193%4^847411200%190%6^847411200%149%9^847411200%178%7^847411200%399%4^847411200%383%5^847411200%30%7^847411200%172%2^847411200%291%9^847411200%157%2^847411200%292%2^847411200%43%0^847411200%80%4^847411200%138%5^
	
	public static NgramResponse dbRecordsToNgramResponse(String ngramName, List<Record> records) {
		List<ListDate> listDates = new ArrayList<ListDate>();
        for (Record r : records) {
//            Integer id = r.getValue(Ngrams.NGRAMS.ID);
//            Timestamp dateFrom = r.getValue(Ngrams.NGRAMS.DATEFROM);
//            Timestamp dateTo = r.getValue(Ngrams.NGRAMS.DATETO);
//            String ngram = r.getValue(Ngrams.NGRAMS.NGRAM);
            String blob = r.getValue(Ngrams.NGRAMS.CONTENT);
            String[] dataPoselIdPartiaIds = blob.split("^");
            for (String dataPoselIdPartiaId : dataPoselIdPartiaIds) {
            	String [] tokens = dataPoselIdPartiaId.split("%");
            	listDates.add(new ListDate(tokens[0], 1));
            }
            
//            System.out.println("ID: " + id + " ngram: " + ngram + " date from: " + firstName + " dateto: " + lastName + " blob: " + blob);
        }
		List<PartiesNgrams> partiesNgrams = new ArrayList<PartiesNgrams>();
		partiesNgrams.add(new PartiesNgrams("nazwa_partii", listDates));

		return new NgramResponse(ngramName, partiesNgrams);
	}
}
