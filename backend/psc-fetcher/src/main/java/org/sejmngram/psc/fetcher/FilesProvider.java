package org.sejmngram.psc.fetcher;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FilesProvider {

    static String[] paths = {
            "../psc-data/pierwsza.xml",
            "../psc-data/druga.xml",
            "../psc-data/trzecia.xml",
            "../psc-data/czwarta.xml",
            "../psc-data/piata.xml",
            "../psc-data/szosta.xml"
    };

    static int getFilesCount() {
       return paths.length;
    }

    static String provideFileContentForIndex(int index) {
        try {
            File file = FileUtils.getFile(paths[index]);
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
