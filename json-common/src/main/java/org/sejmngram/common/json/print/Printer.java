package org.sejmngram.common.json.print;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.sejmngram.common.Configuration;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Dokument;
import org.sejmngram.common.json.datamodel.Wystapienie;

public class Printer {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			Configuration.getInstance().getCommonOutputFilenameDatePattern());
	private static final String OUTPUT_DIR = Configuration.getInstance().getOutputDir();
	
	public static void printCommonJsonsToFiles(List<Wystapienie> wystapienia) {
		try {
			File dir = new File(OUTPUT_DIR);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			Map<String, Dokument> dokumenty = new HashMap<String, Dokument>();
			for (Wystapienie wystapienie : wystapienia) {
				String date = DATE_FORMAT.format(wystapienie.getData());
				if (!dokumenty.containsKey(date)) {
					dokumenty.put(date, new Dokument());
				}
				dokumenty.get(date).addWystapienie(wystapienie);
			}
			for (String date : dokumenty.keySet()) {
				JsonProcessor.printToFile(OUTPUT_DIR + "/" + date + Configuration.getInstance().getCommonOutputFilenameEnding(), dokumenty.get(date));
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
