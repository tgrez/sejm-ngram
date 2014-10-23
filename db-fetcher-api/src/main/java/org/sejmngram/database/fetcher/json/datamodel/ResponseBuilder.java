package org.sejmngram.database.fetcher.json.datamodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.sejmngram.common.json.JsonProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(ResponseBuilder.class);

	private static final Map<String, Integer> initialDates =
			Collections.unmodifiableMap(readDateFile());

	private String ngramName = "";
	private Map<String, TreeMap<String, Integer>> partiesMap =
			new HashMap<String, TreeMap<String, Integer>>();
	
	public ResponseBuilder(String ngramName) {
		this.ngramName = ngramName;
	}
	
	private static Map<String, Integer> readDateFile() {
		Map<String, Integer> initialDates = new HashMap<String, Integer>();
		// TODO make configurable, it seems like it is time to introduce Spring...
		String filename = "../psc-data/nowe_daty.txt";
		HashSet<String> dates = new HashSet<String>();
		try {
			dates = JsonProcessor.jsonFileToHashSet(filename);
		} catch (IOException e) {
			LOG.error("Could not read JSON file: " + filename + ", exception was thrown: ", e);
			return new HashMap<String, Integer>();
		}
		for (String date : dates) {
			initialDates.put(date, 0);
		}
		LOG.info("Successfully loaded dates file from " + filename);
		return initialDates;
	}
	
	public void addOccurance(String partyName, String date) {
		addOccurances(partyName, date, 1);
	}
	
	public void addOccurances(String partyName, String date, int occurances) {
		if (!partiesMap.containsKey(partyName)) {
			partiesMap.put(partyName, new TreeMap<String, Integer>(initialDates));
		}
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
