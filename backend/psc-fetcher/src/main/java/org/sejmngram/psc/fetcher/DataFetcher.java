package org.sejmngram.psc.fetcher;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sejmngram.common.json.JsonProcessor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;

public class DataFetcher {

    static int i = 0;

    public static void main( String[] args ) {
        fetchData();
    }

    private static void fetchData() {
        int filesCount = FilesProvider.getFilesCount();
        for (int i = 0; i < filesCount; i++) {
            String fileContent = FilesProvider.provideFileContentForIndex(i);
            Hashtable<String, String> addresses = parseMetadata(fileContent);
            ArrayList<OpisPosla> poslowie = fetchDataFromWeb(addresses);
            serializeToFile(poslowie, i+1);
        }
    }

    private static Hashtable<String, String> parseMetadata(String content) {
        Document doc =  Jsoup.parse(content);
        Elements elements = doc.select("tr");
        Hashtable<String, String> addresses = new Hashtable<String, String>();
        for (Element tr : elements) {
            String address = tr.select("td a").attr("href");
            String name = tr.select("td a").text();
            addresses.put(name, address);
        }
        i++;
        System.out.println("Parsowanie pliku z posłami " + i + ". kadencji: ");
        System.out.println("Znaleziono link do źródła danych dla " + addresses.size() + " posłów");
        return addresses;
    }

    private static ArrayList<OpisPosla> fetchDataFromWeb(Hashtable<String, String> addresses) {
        ArrayList<OpisPosla> poslowie = new ArrayList<OpisPosla>();
        for (String name : addresses.keySet()) {
            try {
                URI basicUrl = new URI("http","orka.sejm.gov.pl" + addresses.get(name), null, null);
                String data = IOUtils.toString(basicUrl, "ISO-8859-2");
                OpisPosla opis = DataParser.parseDataToOpisPosla(data);
                poslowie.add(opis);
                System.out.print(".");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
        System.out.println("Pobrano z sieci dane " + poslowie.size() + " posłów");
        return poslowie;
    }

    private static void serializeToFile(ArrayList<OpisPosla> poslowie, int i) {
        try {
//            System.out.print(JsonProcessor.transformToJson(poslowie));
            JsonProcessor.printToFile("../psc-data/poslowie_" + i + ".json", poslowie);
            System.out.println("Zapisano do pliku ../psc-data/poslowie_" + i + ".json dane " + poslowie.size() + " posłów");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
