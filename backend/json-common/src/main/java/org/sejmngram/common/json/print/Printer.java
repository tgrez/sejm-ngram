package org.sejmngram.common.json.print;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Dokument;

public class Printer {

	public static void printCommonJsonToFile(Dokument dokument) {
		System.out.println();
		System.out.println("### CONTENT WRITTEN TO FILE ###");
		System.out.println();
		try {
			System.out.println(JsonProcessor.transformToJson(dokument));
			System.out.println();
			System.out.println("### WRITING TO FILE ###");
			System.out.println();
			String filename = "output.json";
			JsonProcessor.printToFile(filename, dokument);
			System.out.println("### CONTENT WRITTEN SUCCESFULLY TO FILE: " + filename + " ###");
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
