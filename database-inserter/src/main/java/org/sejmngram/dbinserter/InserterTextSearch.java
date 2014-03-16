package org.sejmngram.dbinserter;

import org.sejmngram.dbinserter.blobs.BlobCreator;
import org.sejmngram.dbinserter.db.DatabaseRepo;
import org.sejmngram.dbinserter.model.RowData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by michalsiemionczyk on 14/02/14.
 */
public class InserterTextSearch {

    public static void main (String[] args){

/*        HashMap<String, RowData> blobMap = null;
        try {
            blobMap = BlobCreator.getMapOfBlobs("../jsonModifiedData/", 150);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BlobCreator.performAnalysis( blobMap );*/


        File dirPath = new File( "./scripts/sejmometr/dataFromCorpus/processed");
        File[] files = dirPath.listFiles();


        DatabaseRepo dbRepo = new DatabaseRepo("ngramsTSBatch");

        System.out.println( dbRepo.processTresc("zażółć gęślą jaźń" ));

        /*int nrFiles = files.length;
        int i = 0;
        for ( File f : files){
            System.out.println("Processing file " + i  + " out of " + nrFiles);

            dbRepo.insertJsonFileToDb( f);
            i++;
        }*/
    }



}
