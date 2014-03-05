package org.sejmngram.psc.fetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.print.Printer;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class DataParser {

    static OpisPosla parseDataToOpisPosla(String data) {
        Document document = Jsoup.parse(data);
        OpisPosla opisPosla = new OpisPosla();
        parseName(opisPosla, document);
        parseStanowisko(opisPosla, document);
        parseKlub(opisPosla, document);
        return opisPosla;
    }

    private static void parseName(OpisPosla opisPosla, Document document) {
        opisPosla.setPosel(document.select(".Posel").text());
    }

    private static void parseStanowisko(OpisPosla opisPosla, Document document) {
        Elements elements = document.select(":contains(Funkcja w Sejmie)");
        int size = elements.size();
        String stanowisko = "Poseł";
        String objecie = null;
        if (size > 1) {
            String description = elements.get(size - 2).text();
            String[] split = description.split("Funkcja w Sejmie:");
            if (split.length > 1) {
                String[] ods = split[1].split("od");
                if (ods.length > 1) {
                    objecie = ods[1].split("Liczba")[0].trim();
                    stanowisko = ods[0].trim();
                }
                else
                    stanowisko = split[1].trim();
            }
        }
        opisPosla.setStanowisko(stanowisko);
        opisPosla.setObjecie_stanowiska(objecie);
    }

    private static void parseKlub(OpisPosla opisPosla, Document document) {
        opisPosla.setPartia(document.select(".Klub").text());
    }

}
