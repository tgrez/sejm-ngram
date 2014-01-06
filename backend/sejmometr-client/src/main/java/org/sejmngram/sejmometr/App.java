package org.sejmngram.sejmometr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.sejmngram.common.Configuration;
import org.sejmngram.sejmometr.client.RestClient;
import org.sejmngram.sejmometr.transform.TransformExecutor;

public class App {
	public static void main(String[] args) {
		try {
			List<String> responses = new ArrayList<String>();
			for (Integer id : Configuration.getInstance().getSejmometrIds()) {
				responses.add(RestClient.getWystapienie(id));
			}
			TransformExecutor.process(responses);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
