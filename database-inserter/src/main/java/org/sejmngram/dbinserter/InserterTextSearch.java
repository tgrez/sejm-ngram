package org.sejmngram.dbinserter;

import org.apache.commons.lang.math.RandomUtils;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.database.fetcher.json.datamodel.ListDate;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.PartiesNgrams;
import org.sejmngram.dbinserter.blobs.BlobCreator;
import org.sejmngram.dbinserter.db.DatabaseRepo;
import org.sejmngram.dbinserter.model.RowData;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by michalsiemionczyk on 14/02/14.
 */
public class InserterTextSearch {

    public static void main (String[] args){

        HashMap<String, RowData> blobMap = null;
        try {
            blobMap = BlobCreator.getMapOfBlobs("./scripts/sejmometr/dataFromCorpus/processed", 150);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BlobCreator.performAnalysis( blobMap );


//        File dirPath = new File( "./scripts/sejmometr/dataFromCorpus/processed");
//        File[] files = dirPath.listFiles();


//        DatabaseRepo dbRepo = new DatabaseRepo("ngramsTSBatch");

//        System.out.println( dbRepo.processTresc("zażółć gęślą jaźń" ));


        long beginDate = 689835600000l;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");



        String ngram = "haha";

        List<ListDate> dates = new ArrayList<ListDate>();
        dates.add( new ListDate(  sdf.format(new Date(beginDate + RandomUtils.nextInt())), RandomUtils.nextInt(200)));
        dates.add( new ListDate(  sdf.format(new Date(beginDate + RandomUtils.nextInt())), RandomUtils.nextInt(200) ));
        dates.add( new ListDate(  sdf.format(new Date(beginDate + RandomUtils.nextInt())), RandomUtils.nextInt(200) ));
        dates.add( new ListDate(  sdf.format(new Date(beginDate + RandomUtils.nextInt())), RandomUtils.nextInt(200) ));

        List<ListDate> dates2 = new ArrayList<ListDate>();
        dates2.add( new ListDate(  sdf.format(new Date(beginDate + RandomUtils.nextInt())), RandomUtils.nextInt(200)) );
        dates2.add( new ListDate(  sdf.format(new Date(beginDate + RandomUtils.nextInt())), RandomUtils.nextInt(200) ));
        dates2.add( new ListDate(  sdf.format(new Date(beginDate + RandomUtils.nextInt())), RandomUtils.nextInt(200) ));
        dates2.add( new ListDate(  sdf.format(new Date(beginDate + RandomUtils.nextInt())), RandomUtils.nextInt(200) ));

        List<PartiesNgrams> listParties = new ArrayList<PartiesNgrams>();



        listParties.add( new PartiesNgrams("PO", dates));
        listParties.add( new PartiesNgrams("PIS", dates2));

        NgramResponse response = new NgramResponse( "nana", listParties  );


        try {
            for ( String ngramName : blobMap.keySet())




            JsonProcessor.printToFile("./scripts/sejmometr/generatedJSON/file.json", response);
        } catch (IOException e) {
            e.printStackTrace();
        }



        /*int nrFiles = files.length;
        int i = 0;
        for ( File f : files){
            System.out.println("Processing file " + i  + " out of " + nrFiles);

            dbRepo.insertJsonFileToDb( f);
            i++;
        }*/
    }



}
