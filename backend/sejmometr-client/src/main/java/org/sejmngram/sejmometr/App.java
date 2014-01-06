package org.sejmngram.sejmometr;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.sejmngram.sejmometr.client.RestClient;
import org.sejmngram.sejmometr.transform.TransformExecutor;

public class App {
	public static void main(String[] args) {
		try {
			// TODO execute for a list of ids read from properties
			String responseString = RestClient.getWystapienie(11);
			TransformExecutor.process(responseString);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
