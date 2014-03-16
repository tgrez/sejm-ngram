package org.sejmngram.dbinserter.blobs;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.type.TypeReference;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.dbinserter.model.RowData;
import org.sejmngram.dbinserter.utils.Toolkit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class BlobCreator {

    public static HashMap<String, RowData> getMapOfBlobs(String path, int limitFiles)

            throws IOException {
        File dirPath = new File( path);
        File[] files = FileUtils.listFiles(dirPath, new String[]{"json"}, false).toArray(new File[]{});
        assert files != null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        HashMap<String, RowData> blobsMap = new HashMap<String, RowData>();
        int i = 0;
        int nrAllDokuments = files.length;

        ArrayList<String> whiteList = new ArrayList<>(Arrays.asList(whiteWords));

        for (File f : files) {

            if (i > limitFiles && limitFiles > 0) break;
            if (f.getPath().contains("poselId") || f.getPath().contains("partiaId")) break;

            ArrayList<Wystapienie> wystapienia = JsonProcessor.transformFromFile(f, new TypeReference<ArrayList<Wystapienie>>() {});
            System.out.println("processing dokument " + i + " of " + nrAllDokuments);

            if (i % 10 == 0 && i > 0) {
                BlobCreator.performAnalysis( blobsMap );
            }

            for (Wystapienie wyst : wystapienia){

                String[] words = wyst.getTresc().replaceAll("[^\\p{L}\\p{Nd}]", " ").toLowerCase().split(" ");

                ArrayList<String> wordList = new ArrayList<>(Arrays.asList(words));

                for (int j = 1; j < words.length; j++) { // 2gramy
                    wordList.add(words[j - 1] + " " + words[j]);
                }

                for (int j = 2; j < words.length; j++) { // 3gram
                    wordList.add(words[j - 2] + " " + words[j - 1] + " " + words[j]);
                }

                for (int j = 3; j < words.length; j++) { // 4gram
                    wordList.add(words[j - 3] + " " + words[j - 2] + " " + words[j - 1] + " " + words[j]);
                }

//                for (int j = 4; j < words.length; j++) { // 5gram
//                    wordList.add(words[j - 4] + " " + words[j - 3] + " " + words[j - 2] + " " + words[j - 1] + " " + words[j]);
//                }
//
//                for (int j = 5; j < words.length; j++) { // 6gram
//                    wordList.add(words[j - 5] + " " + words[j - 4] + " " + words[j - 3] + " " + words[j - 2] + " " + words[j - 1] + " " + words[j]);
//                }
//
//                for (int j = 6; j < words.length; j++) { // 7gram
//                    wordList.add(words[j - 6] + " " + words[j - 5] + " " + words[j - 4] + " " + words[j - 3] + " " + words[j - 2] + " " + words[j - 1] + " " + words[j]);
//                }

                String unixPosixTimestamp = dateFormat.format(wyst.getData());

                int poselId = Integer.parseInt(wyst.getPosel());
                int partiaId = Integer.parseInt(wyst.getPartia());

                for (String word : wordList){

                    word = word.trim();
                    if (skipWord(word)) continue;
                    if (!whiteList.contains(word)) continue;

                    RowData rowData;

                    if (blobsMap.containsKey(word)) {
                        rowData = blobsMap.get(word);
                    } else {
                        rowData = new RowData();
                    }

                    rowData.addEntryToBlob(unixPosixTimestamp, poselId, partiaId, wyst.getData(), wyst.getData());
                    blobsMap.put(word, rowData);
                }
            }
            i++;

        }
        return blobsMap;
    }

    private static String[] whiteWords = new String[]
            {
                    "poseł kłamie",
                    "kościół",
                    "aborcja",
                    "komuniści",
                    "ku chwale ojczyzny",
                    "niemiecki zaborca",
                    "zamach",
                    "oficer wojska polskiego",
                    "służba bezpieczeństwa",
                    "rosja",
                    "zerwać z przeszłością",
                    "bezpieka",
                    "agenci",
                    "agentów",
                    "wasza wina",
                    "unia europejska",
                    "dług publiczny",
                    "deficyt",
                    "kościół prawosławny",
                    "rodzina",
                    "piwo",
                    "wojna",
                    "piwo",
                    "skandal",
                    "wolność słowa",
                    "ponton",
                    "gmach",
                    "internet",
                    "kanapka",
                    "moralny",
                    "szybko",
                    "lepper",
                    "rodzina",
                    "obniżenie podatków",
                    "próg podatkowy",
                    "bezpieczeństwo obywateli",
                    "prawa czlowieka",
                    "gender",
                    "wcześniejsza emerytura",
                    "zus",
                    "rozwody",
                    "nadzieja",
                    "wybory",
                    "wcześniejsze wybory",
                    "wysoka izbo",
                    "jeszcze polska nie zginęła",
                    "ofe",
                    "reforma",
                    "nfz"
            };

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
