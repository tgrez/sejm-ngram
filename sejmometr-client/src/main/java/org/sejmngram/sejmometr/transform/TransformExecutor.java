package org.sejmngram.sejmometr.transform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.common.json.print.Printer;
import org.sejmngram.sejmometr.json.datamodel.wystapienie2.WystapienieResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(TransformExecutor.class); 
	
	public static void process(Map<Integer, String> responses)
			throws JsonParseException, JsonProcessingException, IOException {
		List<Wystapienie> wystapienia = new ArrayList<Wystapienie>();
		for (Integer responseId : responses.keySet()) {
			String response = responses.get(responseId);
			Wystapienie wystapienie = SejmometrToCommonTransformer
					.doTransform(parseFromJson(response));
			if (wystapienie != null) {
				wystapienia.add(wystapienie);
			} else {
				LOG.debug("Wystapienie was null for request id: " + responseId);
			}
		}
		Printer.printCommonJsonsToFiles(wystapienia);
	}

	private static WystapienieResponse parseFromJson(String jsonString)
			throws JsonParseException, JsonProcessingException, IOException {
		return JsonProcessor.transform(jsonString, WystapienieResponse.class);
	}
}
