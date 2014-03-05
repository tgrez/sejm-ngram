package org.sejmngram.common.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonProcessor {

	private JsonProcessor() {
	}
	
	private static final Logger LOG = Logger.getLogger(JsonProcessor.class.getName());

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static <T> T transform(String stringFormat, Class<T> clazz)
			throws JsonParseException, JsonProcessingException, IOException {
		return objectMapper.readValue(stringFormat, clazz);
	}

	public static <T> String transformToJson(T jsonPojo)
			throws JsonGenerationException, JsonMappingException, IOException {
		return objectMapper.writeValueAsString(jsonPojo);
	}

	public static <T> void printToFile(String filename, T jsonPojo)
			throws JsonGenerationException, JsonMappingException, IOException {
		LOG.debug("Writing to file: " + filename);
		objectMapper.writeValue(
				new FileOutputStream(new File(filename), true),
				jsonPojo);
	}

    public static <T> T transformFromFile( File file, Class<T> clazz) throws IOException {
        return objectMapper.readValue( file, clazz);
    }

    public static <T> T readFromFile(File file, T jsonPojo)
            throws JsonGenerationException, JsonMappingException, IOException {
        LOG.debug("Reading from file: " + file);
        return (T) objectMapper.readValue(file, jsonPojo.getClass());
    }
	
}