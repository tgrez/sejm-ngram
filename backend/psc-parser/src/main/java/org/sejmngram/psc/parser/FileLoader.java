package org.sejmngram.psc.parser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joox.JOOX;
import org.joox.Match;
import org.sejmngram.psc.parser.data.DataFetcher;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.List;

public class FileLoader {

    public static void main( String[] args ) {

        fetchData();
//        parsePSC();

    }

    private static void fetchData() {

        Hashtable<String,String> pierwsza = new Hashtable<String, String>();

        parseMetadata(pierwsza);

        fetchDataFromWeb(pierwsza);


    }

    private static void parseMetadata(Hashtable<String, String> pierwsza) {
        try {

            File pierwszaFile = FileUtils.getFile("../psc-data/pierwsza.xml");
            String mainFile = FileUtils.readFileToString(pierwszaFile);
            List<Element> elements = JOOX.$(mainFile).find("tr").get();
            for (Element tr : elements) {
                String address = JOOX.$(tr).find("td").find("a").attr("href");
                String name = JOOX.$(tr).find("td").find("a").content();
                pierwsza.put(name,address);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fetchDataFromWeb(Hashtable<String, String> pierwsza) {
        for (String name : pierwsza.keySet()) {
            URI basicUrl = null;
            try {
                basicUrl = new URI("http","orka.sejm.gov.pl" + pierwsza.get(name), null, null);
                String data = IOUtils.toString(basicUrl, "ISO-8859-2");
                String klub = data.split("class=\"Klub\"")[1].split("<HR COLOR=\"black\"")[0].split(">")[1].replace("&quot;","\"");
                System.out.println(name + " należy do " + klub);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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
