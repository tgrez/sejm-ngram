package org.sejmngram.server.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.sejmngram.common.json.JsonProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SejmFilesConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SejmFilesConfiguration.class);

    private final String partyFilename;
    private final String poselFilename;
    private final String datesFilename;

    public SejmFilesConfiguration(String partyFilename,
            String poselFilename, String datesFilename) {
        this.partyFilename = partyFilename;
        this.poselFilename = poselFilename;
        this.datesFilename = datesFilename;
    }

    public String getPartyFilename() {
        return partyFilename;
    }

    public String getPoselFilename() {
        return poselFilename;
    }

    public String getDatesFilename() {
        return datesFilename;
    }

    public Set<String> readDatesFromFile() {
        if (datesFilename == null) {
            LOG.error("Could not read dates. Dates filename was not specified in configuration.");
            return new HashSet<String>();
        }
        Set<String> dates;
        try {
            dates = JsonProcessor.jsonFileToHashSet(datesFilename);
        } catch (IOException e) {
            LOG.error("Could not read JSON file: " + datesFilename
                    + ", exception was thrown: ", e);
            return new HashSet<String>();
        }
        LOG.info("Successfully loaded dates file from " + datesFilename);
        return dates;
    }
}
