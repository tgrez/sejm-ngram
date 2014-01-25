package org.sejmngram.psc.parser;

import org.apache.commons.io.FileUtils;
import org.joox.JOOX;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

public class FileLoader {

    public static void main( String[] args ) {
        parsePSC();
    }

    private static void parsePSC() {
        File headerFile = FileUtils.getFile("../psc-data/01-pp-001-01/header.xml");
        File textFile = FileUtils.getFile("../psc-data/01-pp-001-01/text_structure.xml");
        String header = null;
        String text = null;
        try {
            header = FileUtils.readFileToString(headerFile);
            text = FileUtils.readFileToString(textFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Element> osoby = JOOX.$(header).find("person").get();

        Hashtable<String, String> poslowie = new Hashtable<String, String>(osoby.size());

        for (Element posel : osoby) {

            String persName = JOOX.$(posel).find("persName").first().content();
            String attr = JOOX.$(posel).attr("id");
            poslowie.put(attr, persName);

        }

        List<Element> wystapienia = JOOX.$(text).find("body").find("div").get();

        for (Element wystąpienie : wystapienia) {

            List<Element> wypowiedzi = JOOX.$(wystąpienie).find("u").get();

            for (Element wypowiedz : wypowiedzi) {

                String who = JOOX.$(wypowiedz).attr("who");

                if (who != null) {
                    if (!who.equalsIgnoreCase("#komentarz") && !who.equalsIgnoreCase("#ZebraniWstajaZMiejscISpiewaja")) {

                        System.out.println(poslowie.get(who.replace("#","")) + ": " + JOOX.$(wypowiedz).content());

                    } else {



                    }
                }
            }

        }
    }

}
