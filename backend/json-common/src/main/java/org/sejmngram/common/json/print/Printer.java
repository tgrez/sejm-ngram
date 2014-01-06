package org.sejmngram.common.json.print;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Dokument;

public class Printer {

	// TODO read common output filename from properties file
	public static final String filename = "output.json";
	
	public static void printCommonJsonToFile(Dokument dokument) {
		try {
			JsonProcessor.printToFile(filename, dokument);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
