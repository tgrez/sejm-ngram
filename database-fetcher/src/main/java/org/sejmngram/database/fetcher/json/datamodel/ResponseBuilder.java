package org.sejmngram.database.fetcher.json.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseBuilder {

	private String ngramName = "";
	private Map<String, HashMap<String, Integer>> partiesMap = 
			new HashMap<String, HashMap<String, Integer>>();
	
	public ResponseBuilder(String ngramName) {
		this.ngramName = ngramName;
	}
	
	public void addOccurance(String partyName, String date) {
		if (!partiesMap.containsKey(partyName)) {
			partiesMap.put(partyName, new HashMap<String, Integer>());
		}
		Integer previousCount = partiesMap.get(partyName).get(date);
		if (previousCount == null) {
			previousCount = 0;
		}
		++previousCount;
		partiesMap.get(partyName).put(date, previousCount);
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
