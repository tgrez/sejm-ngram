package org.sejmngram.dbinserter.blobs;

import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Dokument;
import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.dbinserter.model.RowData;
import org.sejmngram.dbinserter.utils.Toolkit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by michalsiemionczyk on 05/03/14.
 */
public class BlobCreator {


    /** @param limitFiles 0 for no limit*/
    public static HashMap<String, RowData> getMapOfBlobs(
            String path, int limitFiles, boolean randomIntegerDataForIDs,
            DualHashBidiMap poselIdNameMap, DualHashBidiMap partiaIdNameMap
    )

            throws IOException {
        File dirPath = new File( path);
        File[] files = dirPath.listFiles();

        HashMap<String, RowData> blobsMap = new HashMap<String, RowData>();
        int i = 0 ;
        int nrAllDokuments = files.length;

        for (File f : files){
            //check limit
            if (i > limitFiles && limitFiles > 0) break;

            //create Dokument out of it
            Dokument d = JsonProcessor.transformFromFile(f, Dokument.class);
            System.out.println("processing dokument " + i + " of " + nrAllDokuments);

            //for analysis
            if ( i % 10 == 0 && i > 0) {
                BlobCreator.performAnalysis( blobsMap );
            }

            for (Wystapienie wyst : d.getWystapienia()){
                String[] words = wyst.getTresc().replaceAll("[^\\p{L}\\p{Nd}]", " ").split(" ");
                long unixPosixTimestamp = wyst.getData().getTime() / 1000;
                String posel = wyst.getPosel();
                String partia = wyst.getPartia();

                for (String word : words){

                    word = word.trim();

                    if (skipWord( word )) continue;   //apply skip rules

                    RowData rowData = null;

                    //check it word exists
                    if ( blobsMap.containsKey( word )){
                        rowData = blobsMap.get( word );
                    } else {
                        rowData = new RowData();

                        //sets dateFrom
                        rowData.setDateFrom( wyst.getData() );
                        rowData.setDateTo( wyst.getData());
                    }

                    //rowData.inreaseNrEntries();

                    rowData.addEntryToBlob( unixPosixTimestamp, posel, partia, randomIntegerDataForIDs );

                    //save blob to hashmap
                    blobsMap.put( word, rowData );
                }
            }
            i++;

        }
        return blobsMap;
    }


    public static void performAnalysis(HashMap<String,RowData> blobsMap) {

        System.out.println("------------ STATISTICS start -----------------------");
        System.out.println( "\nnr different words:" + blobsMap.keySet().size());
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        String keyMin = "";
        String keyMax = "";
        ArrayList<Integer> entriesSizes = new ArrayList<Integer>();
        long blobBytes = 0;

        for ( String key :  blobsMap.keySet() ){
            RowData d = blobsMap.get( key );

            blobBytes += Toolkit.getStringSizeInBytes(d.getLastBlob());

            int keySize = blobsMap.get( key ).getNrAllEntries();
            entriesSizes.add( keySize );
            if ( keySize > max ){
                max = keySize;
                keyMax = key;
            }
            if ( keySize < min ){
                min = keySize;
                keyMin = key;
            }
        }

        System.out.println( "\nblobs in [KB]: "+ blobBytes / 1024 + " in [MB]: " + blobBytes/(1024*1024));

        System.out.println( "\nmin nrOccurences" + min + " for : " + keyMin);
        System.out.println( "\nmax  nrOccurences" + max + " for: "+ keyMax );
        System.out.println("\nblob for max:" + blobsMap.get( keyMax ).getLastBlob());


        System.out.println( "\n-- 10 top blob enrties nr: --");
        Comparator<Integer> comparator = Collections.reverseOrder();
        Collections.sort(  entriesSizes, comparator );
        for ( int i = 0; i < 10; i++){
            System.out.println( "blob entries:" + entriesSizes.get( i));
        }

        System.out.println("------------ STATISTICS end -----------------------");


    }

    /** Applies rules of skipping word*/
    private static boolean skipWord( String word){
        //cehck if not length of 1
        if ( word.length() == 1) return true;

        return false;
    }
}
