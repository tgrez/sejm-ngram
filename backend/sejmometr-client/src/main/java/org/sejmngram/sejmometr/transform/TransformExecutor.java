package org.sejmngram.sejmometr.transform;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.print.Printer;
import org.sejmngram.sejmometr.json.datamodel.SejmometrWystapienie;

public class TransformExecutor {

	public static void process(String responseString)
			throws JsonParseException, JsonProcessingException, IOException {
		SejmometrWystapienie sejmometrWystapienie = parseFromJson(responseString);
		Printer.printCommonJsonToFile(SejmometrToCommonTransformer
				.doTransform(sejmometrWystapienie));
	}

	private static SejmometrWystapienie parseFromJson(String jsonString)
			throws JsonParseException, JsonProcessingException, IOException {
		return JsonProcessor.transform(jsonString, SejmometrWystapienie.class);
	}
}
