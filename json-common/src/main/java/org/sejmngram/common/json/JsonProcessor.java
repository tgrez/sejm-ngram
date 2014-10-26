package org.sejmngram.common.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonProcessor {

	private JsonProcessor() {
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(JsonProcessor.class); 

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

    public static <T> T transformFromFile( File file, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(file, typeReference);
    }

    public static <T> T readFromFile(File file, T jsonPojo)
            throws JsonGenerationException, JsonMappingException, IOException {
        LOG.debug("Reading from file: " + file);
        return (T) objectMapper.readValue(file, jsonPojo.getClass());
    }

    public static HashMap<String, String> jsonFileToHashMap( String path) throws IOException {

        HashMap<String, String> map;
        map = objectMapper.readValue(new File ( path),
                new TypeReference<HashMap<String,String>>(){});

        return map;
    }
    
    public static HashSet<String> jsonFileToHashSet(String path) throws IOException {
        HashSet<String> set;
        set = objectMapper.readValue(new File ( path),
                new TypeReference<HashSet<String>>(){});
        return set;
    }
	
}