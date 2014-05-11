package org.sejmngram.dbinserter;

import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.dbinserter.blobs.BlobCreator;
import org.sejmngram.dbinserter.db.DatabaseRepo;
import org.sejmngram.dbinserter.files.JsonFilesReader;
import org.sejmngram.dbinserter.model.RowData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by michalsiemionczyk on 14/02/14.
 */
public class ProcessedWystapieniaInserter {

    public static void main (String[] args){


        String path = "scripts/sejmometr/dataFromCorpus/processed/3/";
        HashSet<String> filesToDiscard = new HashSet<>(Arrays.asList(new String[]{"partiaId.json", "poselId.json", ".DS_Store" }));

        File rootDir = new File( path);
        File[] files = rootDir.listFiles();

        DatabaseRepo dbRepo = new DatabaseRepo();

        int insertedRows = 0;
        int limitROws = 20;
        long startTime = System.currentTimeMillis();
        for (File f : files){
            if (filesToDiscard.contains(f.getName())) continue;
            ArrayList<Wystapienie> wystapienia = JsonFilesReader.getWystapieniesFromJsonFile(f);

            for ( Wystapienie w : wystapienia){
                dbRepo.insertToDb(w);
            }

            insertedRows += wystapienia.size();
            System.out.println("processed file: " + f.getName() +  "and inserted this number of wystapienia: " + wystapienia.size() + ", seconds from start: " + (System.currentTimeMillis() - startTime)/1000);

//            if ( insertedRows > limitROws) break;

        }

        System.out.println("Inserted rows:" + insertedRows);
    }

}
