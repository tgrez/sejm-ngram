package org.sejmngram.psc.parser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.SystemUtils;
import org.joox.JOOX;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.common.json.print.Printer;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileLoader {

    public static void main( String[] args ) {
        parsePSC();
    }

    private static void parsePSC() {

        String basePath = "psc-data";
        File baseDir = FileUtils.getFile(basePath);
        Collection<File> dirs = FileUtils.listFilesAndDirs(baseDir, FileFilterUtils.directoryFileFilter(), TrueFileFilter.INSTANCE);

        dirs.remove(baseDir);

        int kadencja = 0;

        ArrayList partiesList = null;
        HashMap<String, OpisPoslaHelper> partie = null;
        HashSet<String> imionaPoslow = new HashSet<String>();

        for (int i = 1; i < 7; i++) {
            partiesList = readPartiesListFile(i);
            partie = parsePartiesList(partiesList);
            HashSet<String> imionaPoslowTemp = getImionaPoslow(partie);
            imionaPoslow.addAll(imionaPoslowTemp);
        }

        Hashtable<String, String> poslowie = addSpecialPoslowie();
        for (File dir : dirs) {
            String header = readHeaderFile(dir);
            Hashtable<String, String> poslowieTmp = getPoslowie(header);
            poslowie.putAll(poslowieTmp);
        }

        System.out.println("Metadata loaded...");

        int j = 0;

        for (File dir : dirs) {

            System.out.println("Parsing " + dir.getPath() + " ...");

            if (!dir.getPath().startsWith(basePath.concat("/0" + kadencja))) {
                String substring = dir.getPath().replace(basePath + "/0", "").substring(0, 1);
                kadencja = Integer.valueOf(substring).intValue();
                partiesList = readPartiesListFile(kadencja);
                partie = parsePartiesList(partiesList);
            }

            String header = readHeaderFile(dir);
            String text = readContentFile(dir);
            Date date = getDate(header);
            String baseTitle = getBaseTitle(header);
            j = 0;

            HashSet<PoselStanowiskoHelper> poselStanowiskoHelperHashSet = new HashSet<PoselStanowiskoHelper>();

            List<Wystapienie> wystapienies = new ArrayList<Wystapienie>();
            List<Element> wystapienia = JOOX.$(text).find("body").find("div").get();
            for (Element wystąpienie : wystapienia) {
                List<Element> wypowiedzi = JOOX.$(wystąpienie).find("u").get();
                for (Element wypowiedz : wypowiedzi) {
                    String who = JOOX.$(wypowiedz).attr("who");
                    if (who != null && !who.equalsIgnoreCase("#komentarz")) {
                        j += 1;
                        String keyForPoslowie = who.replace("#", "");
                        String poselFull = poslowie.get(keyForPoslowie);
                        PoselStanowiskoHelper poselStanowisko = getPoselStanowisko(imionaPoslow, keyForPoslowie, poselFull);
                        poselStanowiskoHelperHashSet.add(poselStanowisko);
                        String tresc = getTresc(wypowiedz);
                        String tytul = getTytul(baseTitle, poselFull);
                        String partia = getPartia(partie, poselStanowisko.posel);
                        String id = createId(date, j);
                        Wystapienie wystapienie = createWystapienie(date, poselStanowisko.posel,
                                poselStanowisko.stanowisko, tresc, tytul, partia, id);
                        wystapienies.add(wystapienie);
                    }
                }
            }

            for (PoselStanowiskoHelper helper : poselStanowiskoHelperHashSet) {
//                System.out.println(helper.posel + " : " + helper.stanowisko);
            }
            Printer.printCommonJsonsToFiles(wystapienies);
        }
    }

    private static String createId(Date date, int j) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return j + sdf.format(date);
    }

    private static Hashtable<String, String> addSpecialPoslowie() {
        Hashtable<String, String> poslowie = new Hashtable<String, String>();
        poslowie.put("GlosZSali", "Głos z sali");
        poslowie.put("SekretarzPoselMarekDomin", "Sekretarz Poseł Marek Domin");
        poslowie.put("WicemarszalekJacekKurczewski", "Wicemarszałek Jacek Kurczewski");
        poslowie.put("WicemarszalekJozefZych", "Wicemarszałek Józef Zych");
        poslowie.put("WicemarszalekDariuszWojcik", "Wicemarszałek Dariusz Wójcik");
        poslowie.put("MinisterSprawWewnetrznychAntoniMacierewicz", "Minister Spraw Wewnętrznych Antoni Macierewicz");
        poslowie.put("PoselBarbaraBlida", "Poseł Barbara Blida");
        return poslowie;
    }

    private static PoselStanowiskoHelper getPoselStanowisko(HashSet<String> imionaPoslow, String keyForPoslowie, String poselFull) {
        PoselStanowiskoHelper poselStanowisko = new PoselStanowiskoHelper();
        if (poselFull != null) {
            String[] poselFullSplitted = poselFull.split("\\ ");
            int i = 0;
            for (; i < poselFullSplitted.length; i++) {
                if (imionaPoslow.contains(poselFullSplitted[i])) break;
            }
            if (i == poselFullSplitted.length) { // nie znaleziono imienia
                poselStanowisko.posel = "";
                poselStanowisko.stanowisko = poselFull;
            } else {
                int indexOfName = poselFull.indexOf(poselFullSplitted[i]);
                if (indexOfName == 0) { // nie znaleziono stanowiska
                    poselStanowisko.posel = poselFull;
                    poselStanowisko.stanowisko = "";
                } else {
                    poselStanowisko.stanowisko = poselFull.substring(0, indexOfName);
                    poselStanowisko.posel = poselFull.substring(indexOfName);
                }
            }
        } else {
            poselStanowisko.posel = keyForPoslowie;
            poselStanowisko.stanowisko = keyForPoslowie;
            System.out.println(keyForPoslowie);
        }
        return poselStanowisko;
    }

    private static HashSet<String> getImionaPoslow(HashMap<String, OpisPoslaHelper> partie) {
        HashSet<String> imionaPoslow = new HashSet<String>();
        for(String imieNazwisko : partie.keySet()) {
            String imieNazwiskoEnhanced = imieNazwisko.replaceFirst("\\ ", "######");
            String[] imieNazwiskoArray = imieNazwiskoEnhanced.split("######");
            String imie = imieNazwiskoArray[0];
            imionaPoslow.add(imie);
        }
        return imionaPoslow;
    }

    private static String getTresc(Element wypowiedz) {
        return JOOX.$(wypowiedz).content();
    }

    private static String getPartia(HashMap<String, OpisPoslaHelper> partie, String posel) {
        String partia = "";
        if (partie.get(posel) != null) {
            partia = partie.get(posel).partia;
        }
        return partia;
    }

    private static Wystapienie createWystapienie(Date date, String posel, String stanowisko, String tresc, String tytul, String partia, String id) {
        Wystapienie wystapienie = new Wystapienie();
        wystapienie.setPosel(posel);
        wystapienie.setData(date);
        wystapienie.setPartia(partia);
        wystapienie.setStanowisko(stanowisko);
        wystapienie.setTresc(tresc);
        wystapienie.setTytul(tytul);
        wystapienie.setId(id);
        return wystapienie;
    }

    private static String getTytul(String baseTitle, String poselFull) {
        String titleSuffix = "";
        if (poselFull != null) {
            titleSuffix = " " + poselFull;
        }
        return baseTitle.substring(0, baseTitle.length() - 1) + titleSuffix;
    }

    private static Hashtable<String, String> getPoslowie(String header) {
        List<Element> osoby = JOOX.$(header).find("person").get();
        Hashtable<String, String> poslowie = new Hashtable<String, String>(osoby.size());
        for (Element posel : osoby) {
            String persName = JOOX.$(posel).find("persName").first().content();
            String attr = JOOX.$(posel).attr("id");
            poslowie.put(attr, persName);
        }
        return poslowie;
    }

    private static Date getDate(String header) {
        String dateString = JOOX.$(header).find("sourceDesc").find("bibl")
                .find("date").first().content();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = dateFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    private static String getBaseTitle(String header) {
        String baseTitle = "";
        List<Element> titles = JOOX.$(header).find("sourceDesc").find("bibl").find("title").get();
        for (Element titlElem : titles)
            if (titlElem.getAttribute("xml:lang").equalsIgnoreCase("pl"))
                baseTitle = titlElem.getTextContent();
        return baseTitle;
    }

    private static String readContentFile(File dir) {
        File textFile = FileUtils.getFile(dir.getPath() + "/text_structure.xml");
        String text = null;
        try {
            text = FileUtils.readFileToString(textFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    private static String readHeaderFile(File dir) {
        File headerFile = FileUtils.getFile(dir.getPath() + "/header.xml");
        String header = null;
        try {
            header = FileUtils.readFileToString(headerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return header;
    }

    private static HashMap<String, OpisPoslaHelper> parsePartiesList(ArrayList partiesList) {
        HashMap<String, OpisPoslaHelper> partie = new HashMap<String, OpisPoslaHelper>(partiesList.size());
        for (Object entry : partiesList) {
            String key = ((HashMap<String, String>) entry).get("posel");
            OpisPoslaHelper helper = new OpisPoslaHelper();
            helper.stanowisko = ((HashMap<String, String>) entry).get("stanowisko");
            helper.partia = ((HashMap<String, String>) entry).get("partia");
            helper.objecie_stanowiska = ((HashMap<String, String>) entry).get("objecie_stanowiska");
            partie.put(key, helper);
        }
        return partie;
    }

    private static ArrayList readPartiesListFile(int kadencja) {
        File partiesFile = FileUtils.getFile("psc-data/poslowie_" + kadencja + ".json");
        ArrayList partiesList = new ArrayList();
        try {
            partiesList = JsonProcessor.readFromFile(partiesFile, partiesList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return partiesList;
    }

    private static class OpisPoslaHelper {
        String stanowisko;
        String partia;
        String objecie_stanowiska;
    }

    private static class PoselStanowiskoHelper {
        String stanowisko;
        String posel;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PoselStanowiskoHelper that = (PoselStanowiskoHelper) o;
            if (!posel.equals(that.posel)) return false;
            if (!stanowisko.equals(that.stanowisko)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = stanowisko.hashCode();
            result = 31 * result + posel.hashCode();
            return result;
        }
    }

}
