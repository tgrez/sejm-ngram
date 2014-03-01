package com.example.helloworld.factory;

import java.util.ArrayList;
import java.util.List;

import org.sejmngram.database.fetcher.json.datamodel.ListDate;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.PartiesNgrams;

public class NgramFactory {

	public NgramResponse generateDefaultNgramResponse() {
		return null;
	}

	public NgramResponse generateNgramResponse(String ngramName) {
		List<ListDate> listDates = new ArrayList<ListDate>();
		listDates.add(new ListDate("2011-02-08", 21));
		listDates.add(new ListDate("2011-02-09", 123));
		listDates.add(new ListDate("2011-02-08", 56));
		
		List<PartiesNgrams> partiesNgrams = new ArrayList<PartiesNgrams>();
		partiesNgrams.add(new PartiesNgrams("PiS", listDates));
		return new NgramResponse(ngramName, partiesNgrams);
	}
}
