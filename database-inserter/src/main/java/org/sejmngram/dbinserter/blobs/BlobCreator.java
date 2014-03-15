package org.sejmngram.dbinserter.blobs;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.type.TypeReference;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.dbinserter.model.RowData;
import org.sejmngram.dbinserter.utils.Toolkit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class BlobCreator {

    public static HashMap<String, RowData> getMapOfBlobs(String path, int limitFiles)

            throws IOException {
        File dirPath = new File( path);
        File[] files = dirPath.listFiles();
        assert files != null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        HashMap<String, RowData> blobsMap = new HashMap<String, RowData>();
        int i = 0;
        int nrAllDokuments = files.length;

        for (File f : files) {

            if (i > limitFiles && limitFiles > 0)
                break;

            ArrayList<Wystapienie> wystapienia = JsonProcessor.transformFromFile(f, new TypeReference<ArrayList<Wystapienie>>() {});
            System.out.println("processing dokument " + i + " of " + nrAllDokuments);

            if (i % 10 == 0 && i > 0) {
                BlobCreator.performAnalysis( blobsMap );
            }

            for (Wystapienie wyst : wystapienia){

                String[] words = wyst.getTresc().replaceAll("[^\\p{L}\\p{Nd}]", " ").toLowerCase().split(" ");


                String unixPosixTimestamp = dateFormat.format(wyst.getData());

                int poselId = Integer.parseInt(wyst.getPosel());
                int partiaId = Integer.parseInt(wyst.getPartia());

                for (String word : words){

                    word = word.trim();
                    if (skipWord(word)) continue;
                    RowData rowData;

                    if (blobsMap.containsKey(word)) {
                        rowData = blobsMap.get(word);
                    } else {
                        rowData = new RowData();
                        rowData.setDateFrom(wyst.getData());
                        rowData.setDateTo(wyst.getData());
                    }

                    rowData.addEntryToBlob(unixPosixTimestamp, poselId, partiaId);
                    blobsMap.put(word, rowData);
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

            blobBytes += blobBytes += d.getLastBlob().length;

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
        if (word.length() == 1 || word.length() == 0) return true;

        return false;
    }
}
