package org.sejmngram.database.fetcher.converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.sejmngram.common.json.JsonProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdConverter {

    private static final Logger LOG = LoggerFactory
            .getLogger(IdConverter.class);

    private Map<String, String> idToNameMap = new HashMap<String, String>();

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
            if (idToNameMap.containsKey(idString)) {
                LOG.error("Id: " + idString + " appears more than once in file: '"
                        + filename + "'");
            }
            idToNameMap.put(idString, tmpMap.get(idString));
        }
    }

    public Map<String, String> returnMap(){
        return idToNameMap;
    }

    public String resolve(Integer id) {
        return idToNameMap.get(id);
    }
}
