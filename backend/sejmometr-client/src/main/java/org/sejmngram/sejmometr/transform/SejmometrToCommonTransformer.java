package org.sejmngram.sejmometr.transform;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.sejmngram.common.Configuration;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.sejmometr.client.RestClient;
import org.sejmngram.sejmometr.json.datamodel.klub.KlubResponse;
import org.sejmngram.sejmometr.json.datamodel.wystapienie.WystapienieData;
import org.sejmngram.sejmometr.json.datamodel.wystapienie.WystapienieResponse;

public class SejmometrToCommonTransformer {

	
	private static Map<Integer, String> partyIdToPartyNameMap = new HashMap<Integer, String>();
	
	public static Wystapienie doTransform(WystapienieResponse sejmometrWystapienie)
			throws JsonParseException, JsonProcessingException, IOException {
		WystapienieData dane = sejmometrWystapienie
				.getDocument().getContent().getData();
		if (dane == null) {
			return null;
		}
		Wystapienie output = new Wystapienie();
		output.setData(dane.getData());
		output.setPartia(resolvePartyId(dane.getKlub_id()));
		output.setPosel(dane.getLudzie_nazwa());
		output.setStanowisko(dane.getStanowiska_nazwa());
		output.setTytul(dane.getTytul());
		output.setTresc(sejmometrWystapienie.getDocument().getContent()
				.getLayers().getHtmlText());
		return output;
	}

	private static String resolvePartyId(String klub_id)
			throws JsonParseException, JsonProcessingException, IOException {
		Integer partyId = Integer.parseInt(klub_id);
		if (partyIdToPartyNameMap.containsKey(partyId)) {
			return partyIdToPartyNameMap.get(partyId);
		}
		RestClient sejmometrRestClient = new RestClient(Configuration
				.getInstance().getSejmometrKluby());
		String jsonString = sejmometrRestClient.get(partyId);
		KlubResponse sejmometrResponse = JsonProcessor.transform(
				jsonString, KlubResponse.class);
		String nazwa = sejmometrResponse.getDocument().getContent().getData().getNazwa();
		partyIdToPartyNameMap.put(partyId, nazwa);
		return nazwa;
	}
}
