package org.sejmngram.dbinserter;

import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Dokument;
import org.sejmngram.dbinserter.blobs.BlobCreator;
import org.sejmngram.dbinserter.db.DatabaseRepo;
import org.sejmngram.dbinserter.files.JsonFilesReader;
import org.sejmngram.dbinserter.model.RowData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by michalsiemionczyk on 14/02/14.
 */
public class Inserter {

    public static void main (String[] args){

        HashMap<String, RowData> blobMap = null;
        try {
            blobMap = BlobCreator.getMapOfBlobs("../jsonModifiedData/", -1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BlobCreator.performAnalysis( blobMap );

        DatabaseRepo dbRepo = new DatabaseRepo();
        dbRepo.insertToDb(blobMap);
    }

}
