package org.sejmngram.database.fetcher.connection;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.database.fetcher.converter.IdConverter;
import org.sejmngram.database.fetcher.converter.NgramFtsConverter;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.resource.NgramFtsDao;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySqlFtsDbConnector implements DbConnector {

    private static final Logger LOG = LoggerFactory.getLogger(MySqlFtsDbConnector.class);

    private NgramFtsConverter ngramFtsConverter = null;

    private final DBI jdbi;
    private NgramFtsDao ngramFtsDao;

    public MySqlFtsDbConnector(DBI jdbi, String partyFilename,
            String poselFilename, String datesFilename) {
        this.jdbi = jdbi;
        readFiles(partyFilename, poselFilename, datesFilename);
    }

    @Override
    public void connect() {
        ngramFtsDao = jdbi.onDemand(NgramFtsDao.class);
    }

    @Override
    public void disconnect() {
        ngramFtsDao.close();
    }

    @Override
    public NgramResponse retrieve(String ngram) {
        assert ngramFtsDao != null : "Dao object was not initialized";
        return ngramFtsConverter.dbRecordsToNgramResponse(ngram,
                ngramFtsDao.searchFts(ngram));
    }

    private void readFiles(String partyFilename, String poselFilename, String datesFilename) {
        Set<String> dates = readDatesFile(datesFilename);
        ngramFtsConverter = new NgramFtsConverter(
                new IdConverter(partyFilename), new IdConverter(poselFilename), dates);
    }

    private Set<String> readDatesFile(String filename) {
        Set<String> dates = new HashSet<String>();
        try {
            dates = JsonProcessor.jsonFileToHashSet(filename);
        } catch (IOException e) {
            LOG.error("Could not read JSON file: " + filename
                    + ", exception was thrown: ", e);
            return new HashSet<String>();
        }
        LOG.info("Successfully loaded dates file from " + filename);
        return dates;
    }

    @Override
    public NgramResponse retrieveByParty(String ngram, int partyId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NgramResponse retrieveByPosel(String ngram, int poselId) {
        // TODO Auto-generated method stub
        return null;
    }

}
