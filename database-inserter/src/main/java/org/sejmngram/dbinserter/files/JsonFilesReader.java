package org.sejmngram.dbinserter.files;

import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.codehaus.jackson.type.TypeReference;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Dokument;
import org.sejmngram.common.json.datamodel.Wystapienie;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by michalsiemionczyk on 05/03/14.
 */
public class JsonFilesReader {

    /**
     *
     * @param path
     * @param limitFilesRead 0 for no limit
     * @return
     */

    public static DualHashBidiMap getJsonMapToMap( String path){
        HashMap<String, String> map = null;
        try {
            map = JsonProcessor.jsonFileToHashMap( path );
        } catch (IOException e) {
            e.printStackTrace();
        }

        DualHashBidiMap dMap = new DualHashBidiMap( map);
        return dMap;
    }



    public static ArrayList<Wystapienie> getWystapieniesFromJsonFile( File jsonFile){
        ArrayList<Wystapienie> wystapienia = null;
        try {
             wystapienia = JsonProcessor.transformFromFile(jsonFile, new TypeReference<ArrayList<Wystapienie>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wystapienia;
    }



    public static ArrayList<Dokument> getDokumentFromJsonFile( String path, int limitFilesRead ){

        //for maven build use ".."
        File f = new File( path);
        File[] files = f.listFiles();

        ArrayList<Dokument> dokuments = new ArrayList<Dokument>();

        int filesRead = 0;
        try {
            for ( File jsonFile : files){
                Dokument d = JsonProcessor.transformFromFile(jsonFile, Dokument.class);
                dokuments.add( d );
                System.out.println("ilosc wytapien w pliku json:" + d.getWystapienia().size());
                filesRead++;
                if ( limitFilesRead == filesRead) break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dokuments;
    }
}
