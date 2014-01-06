package org.sejmngram.sejmometr.transform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.common.json.print.Printer;
import org.sejmngram.sejmometr.json.datamodel.SejmometrWystapienie;

public class TransformExecutor {

	public static void process(List<String> responses)
			throws JsonParseException, JsonProcessingException, IOException {
		List<Wystapienie> wystapienia = new ArrayList<Wystapienie>();
		for (String response : responses) {
			Wystapienie wystapienie = SejmometrToCommonTransformer
					.doTransform(parseFromJson(response));
			if (wystapienie != null) {
				wystapienia.add(wystapienie);
			}
		}
		Printer.printCommonJsonsToFiles(wystapienia);
	}

	private static SejmometrWystapienie parseFromJson(String jsonString)
			throws JsonParseException, JsonProcessingException, IOException {
		return JsonProcessor.transform(jsonString, SejmometrWystapienie.class);
	}
}
