package org.sejmngram.common.modifier;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.io.FileUtils;
import org.sejmngram.common.json.JsonProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by krzysztofsiejkowski on 13/03/14.
 */
public class JsonModifier {

    public static void main( String[] args ) {
        modifyJson();
    }

    private static class IndexHelper {
        Integer index;
        String value;
    }

    private static void modifyJson() {
//        String baseInputPath = "../jsonPrinterDir";
        String baseInputPath = "./scripts/sejmometr/dataFromCorpus";
//        String baseOutputDir = "../jsonModifiedData";
        String baseOutputDir = baseInputPath + "/processed";
        File dir = new File(baseOutputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File baseDir = FileUtils.getFile(baseInputPath);
        Collection<File> files = FileUtils.listFiles(baseDir, new String[]{"json"}, false);

        int poslowieIndex = 0;
        int partieIndex = 0;
        DualHashBidiMap poslowie = new DualHashBidiMap();
        DualHashBidiMap partie = new DualHashBidiMap();

        for (File file : files) {
            HashMap map = new HashMap();
            try {
                map = JsonProcessor.readFromFile(file, map);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<HashMap> wystapienia = (ArrayList<HashMap>) map.get("wystapienia");
            for (HashMap wystapienie : wystapienia) {
                String posel = (String) wystapienie.get("posel");
                Integer index = 0;
                if (poslowie.containsValue(posel)) {
                    index = (Integer) poslowie.getKey(posel);
                } else {
                    poslowieIndex += 1;
                    poslowie.put(poslowieIndex, posel);
                    index = poslowieIndex;
                }
                wystapienie.put("posel", index);
                String partia = (String) wystapienie.get("partia");
                index = 0;
                if (partie.containsValue(partia)) {
                    index = (Integer) partie.getKey(partia);
                } else {
                    partieIndex += 1;
                    partie.put(partieIndex, partia);
                    index = partieIndex;
                }
                wystapienie.put("partia", index);
            }

            try {
                JsonProcessor.printToFile(baseOutputDir + "/" + file.getName(), wystapienia);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            JsonProcessor.printToFile(baseOutputDir + "/poselId.json", poslowie);
            JsonProcessor.printToFile(baseOutputDir + "/partiaId.json", partie);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
