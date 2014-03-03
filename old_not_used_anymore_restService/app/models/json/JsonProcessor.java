//package models.json;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import com.fasterxml.jackson.core.JsonGenerationException;
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import play.Logger;
//
//public class JsonProcessor {
//
//	private JsonProcessor() {
//	}
//
//	private static ObjectMapper objectMapper = new ObjectMapper();
//
//	public static <T> T transform(String stringFormat, Class<T> clazz) throws IOException, JsonMappingException {
//		return objectMapper.readValue(stringFormat, clazz);
//	}
//
//	public static <T> String transformToJson(T jsonPojo)
//			throws JsonGenerationException, JsonMappingException, IOException {
//		return objectMapper.writeValueAsString(jsonPojo);
//	}
//
//	public static <T> void printToFile(String filename, T jsonPojo)
//    {
//		Logger.debug("Writing to file: " + filename);
//		objectMapper.writeValue(
//				new FileOutputStream(new File(filename), true),
//				jsonPojo);
//	}
//
//}