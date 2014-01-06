package org.sejmngram.common.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonProcessor {

	private JsonProcessor() {
	}

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static <T> T transform(String stringFormat, Class<T> clazz)
			throws JsonParseException, JsonProcessingException, IOException {
		return objectMapper.readValue(stringFormat, clazz);
	}

}