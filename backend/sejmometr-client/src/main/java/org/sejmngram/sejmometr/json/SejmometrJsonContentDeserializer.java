package org.sejmngram.sejmometr.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.sejmngram.sejmometr.json.datamodel.Data;

public class SejmometrJsonContentDeserializer extends JsonDeserializer<Data>  {

	@Override
	public Data deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {
		if ("false".equals(jsonParser.getText())) {
			return null;
		}
		return jsonParser.readValueAs(Data.class);
	}

}
