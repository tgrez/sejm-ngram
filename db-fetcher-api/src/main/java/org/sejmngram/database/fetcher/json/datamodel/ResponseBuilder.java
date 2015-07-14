package org.sejmngram.database.fetcher.json.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ResponseBuilder {

    private final Map<String, Integer> initialDates;

    private String ngramName = "";
    private Map<String, TreeMap<String, Integer>> partiesMap = new HashMap<String, TreeMap<String, Integer>>();

    public ResponseBuilder(String ngramName) {
        this.ngramName = ngramName;
        this.initialDates = new HashMap<String, Integer>();
    }

    public ResponseBuilder(String ngramName, Set<String> dates) {
        this(ngramName);
        for (String date : dates) {
            this.initialDates.put(date, 0);
        }
    }

    public void addOccurance(String partyName, String date) {
        addOccurances(partyName, date, 1);
    }

    public void addOccurances(String partyName, String date, int occurances) {
        if (!partiesMap.containsKey(partyName)) {
            partiesMap.put(partyName,
                    new TreeMap<String, Integer>(initialDates));
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
                listDates.add(new ListDate(date, partiesMap.get(partyName).get(
                        date)));
            }
            partiesNgrams.add(new PartiesNgrams(partyName, listDates));
        }
        return new NgramResponse(ngramName, partiesNgrams);
    }

    public static NgramResponse emptyResponse(String ngramName) {
        return new ResponseBuilder(ngramName).generateResponse();
    }

    public static NgramResponse emptyResponse(String ngramName, Set<String> dates) {
        return new ResponseBuilder(ngramName, dates).generateResponse();
    }
}
