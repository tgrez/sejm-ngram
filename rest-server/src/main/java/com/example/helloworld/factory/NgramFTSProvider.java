package com.example.helloworld.factory;

import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.connection.MySqlFtsDbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

public class NgramFTSProvider {

    private DbConnector db;

    public NgramFTSProvider() {
        db = new MySqlFtsDbConnector();
        db.connect();
    }


    public NgramResponse generateDefaultNgramResponse(String ngramName) {

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
