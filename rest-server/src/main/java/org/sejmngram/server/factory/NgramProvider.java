package org.sejmngram.server.factory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.connection.MySqlDbConnector;
import org.sejmngram.database.fetcher.json.datamodel.ListDate;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.PartiesNgrams;

public class NgramProvider {

    private DbConnector db;

    public NgramProvider() {
        db = new MySqlDbConnector();
        db.connect();
    }


    public NgramResponse generateRandomNgramResponse(String ngramName) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<PartiesNgrams> partiesNgrams = new ArrayList<PartiesNgrams>();

        for (int j = 0; j < RandomUtils.nextInt(9)+ 1 ; j++) {

            List<ListDate> listDates = new ArrayList<ListDate>();

            for (int i = 0; i < RandomUtils.nextInt(200); i++) {
                long beginDate = 689835600000l;
                listDates.add(new ListDate(sdf.format(new Date(beginDate + RandomUtils.nextInt())), RandomUtils.nextInt((200))));
            }

            partiesNgrams.add(new PartiesNgrams("Party" + j, listDates));
        }
        return new NgramResponse(ngramName, partiesNgrams);
    }

    public NgramResponse generateNgramResponse(String ngramName) {
        return db.retrieve(ngramName);
    }
}
