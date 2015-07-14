package org.sejmngram.database.fetcher.connection;

import java.util.Set;

import org.sejmngram.database.fetcher.converter.IdConverter;
import org.sejmngram.database.fetcher.converter.NgramFtsConverter;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.resource.NgramFtsDao;
import org.skife.jdbi.v2.DBI;

public class MySqlFtsDbConnector implements DbConnector {

    private NgramFtsConverter ngramFtsConverter = null;

    private final DBI jdbi;
    private NgramFtsDao ngramFtsDao;

    public MySqlFtsDbConnector(DBI jdbi, String partyFilename,
            String poselFilename, Set<String> dates) {
        this.jdbi = jdbi;
        this.ngramFtsConverter = new NgramFtsConverter(
                new IdConverter(partyFilename), new IdConverter(poselFilename), dates);
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
