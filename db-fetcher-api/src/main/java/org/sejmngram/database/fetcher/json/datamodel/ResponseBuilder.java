package org.sejmngram.database.fetcher.json.datamodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.sejmngram.common.json.JsonProcessor;

public class ResponseBuilder {

	private String ngramName = "";
	private Map<String, TreeMap<String, Integer>> partiesMap =
			new HashMap<String, TreeMap<String, Integer>>();
	
	private HashSet<String> dates = new HashSet<String>();
	private HashMap<String, Integer> initialDates = new HashMap<String, Integer>();
	
	public ResponseBuilder(String ngramName) {
		this.ngramName = ngramName;
		readDateFile();
	}
	
	private void readDateFile() {
		try {
			dates = JsonProcessor.jsonFileToHashSet("../psc-data/nowe_daty.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String date : dates) {
			initialDates.put(date.substring(0, 7) + "-01", 0);
		}
	}
	
	public void addOccurance(String partyName, String date) {
		addOccurances(partyName, date, 1);
	}
	
	public void addOccurances(String partyName, String date, int occurances) {
		if (!partiesMap.containsKey(partyName)) {
			partiesMap.put(partyName, new TreeMap<String, Integer>(initialDates));
		}
		date = date.substring(0, 7) + "-01";
		Integer count = partiesMap.get(partyName).get(date);
		if (count == null) {
			count = 0;
		}
		count += occurances;
		partiesMap.get(partyName).put(date, count);
	}
	
	public NgramResponse generateResponse() {
		List<PartiesNgrams> partiesNgrams = new ArrayList<PartiesNgrams>();
		for (String partyName : partiesMap.keySet()) {
			List<ListDate> listDates = new ArrayList<ListDate>();
			for (String date : partiesMap.get(partyName).keySet()) {
				listDates.add(new ListDate(date, partiesMap.get(partyName).get(date)));
			}
			partiesNgrams.add(new PartiesNgrams(partyName, listDates));
		}
		return new NgramResponse(ngramName, partiesNgrams);
	}
}
