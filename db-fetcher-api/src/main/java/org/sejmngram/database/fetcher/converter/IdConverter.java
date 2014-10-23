package org.sejmngram.database.fetcher.converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sejmngram.common.json.JsonProcessor;

public class IdConverter {
	
	private static final Logger LOG = Logger.getLogger(IdConverter.class);
	
	private Map<Integer, String> idToNameMap = new HashMap<Integer, String>();
	
	public IdConverter(String filename) {
		HashMap<String, String> tmpMap = null;
		try {
			tmpMap = JsonProcessor.jsonFileToHashMap(filename);
		} catch (IOException e) {
			LOG.error("Could not read file: '" + filename + "'", e);
		}
		if (tmpMap == null) {
			LOG.error("Could not read file: '" + filename + "'");
		}
		for (String idString : tmpMap.keySet()) {
			Integer id = Integer.valueOf(idString);
			if (idToNameMap.containsKey(id)) {
				LOG.error("Id: " + id + " appears more than once in file: '" + filename + "'");
			}
			idToNameMap.put(id, tmpMap.get(idString));
		}
	}
	
	public String resolve(Integer id) {
		return idToNameMap.get(id);
	}
}
