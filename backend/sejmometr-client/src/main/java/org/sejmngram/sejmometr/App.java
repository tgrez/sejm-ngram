package org.sejmngram.sejmometr;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.sejmngram.common.Configuration;
import org.sejmngram.sejmometr.client.RestClient;
import org.sejmngram.sejmometr.transform.TransformExecutor;

public class App {
	
	private static final RestClient sejmometrRestClient = new RestClient(
			Configuration.getInstance().getSejmometrWystapienia());
	
	public static void main(String[] args) {
		try {
			// TODO how to read properties file
			Map<Integer, String> responses = new HashMap<Integer, String>();
			for (Integer id : Configuration.getInstance().getSejmometrIds()) {
				responses.put(id, sejmometrRestClient.get(id));
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
