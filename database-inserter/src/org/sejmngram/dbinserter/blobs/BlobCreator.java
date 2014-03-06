package org.sejmngram.dbinserter.blobs;

import org.sejmngram.common.json.datamodel.Dokument;
import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.dbinserter.model.RowData;
import org.sejmngram.dbinserter.utils.Toolkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by michalsiemionczyk on 05/03/14.
 */
public class BlobCreator {

    public static HashMap<String, RowData> getMapOfBlobs(ArrayList<Dokument> dokuments){

//        unixtimestamp(seconds)%Cezary Grarczyk%PlatformaObywatelska^unixtimestamp%AleksanderOlechowski%PlatformaObywatelska^
        HashMap<String, RowData> blobsMap = new HashMap<String, RowData>();
        int i = 0 ;
        int nrAllDokuments = dokuments.size();

        for ( Dokument d : dokuments ){
            System.out.println("processing dokument " + i + " of " + nrAllDokuments);
            for ( Wystapienie wyst : d.getWystapienia()){
                String[] words = wyst.getTresc().split(" ");
                long unixPosixTimestamp = wyst.getData().getTime() / 1000;
                String posel = wyst.getPosel();
                String partia = wyst.getPartia();

                for ( String word : words ){

                    if ( skipWord( word )) continue;;   //apply skip rules

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
                    rowData.addEntryToBlob( unixPosixTimestamp, posel, partia );

                    //save blob to hashmap
                    blobsMap.put( word, rowData );
                }
            }
            i++;

        }

        return blobsMap;
    }

    public static void performAnalysis(HashMap<String,RowData> blobsMap) {
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

    }

    /** Applies rules of skipping word*/
    private static boolean skipWord( String word){
        //cehck if not length of 1
        if ( word.length() == 1) return true;

        return false;
    }
}
