package org.sejmngram.sejmometr.transform;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.sejmngram.common.Configuration;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.sejmometr.client.RestClient;
import org.sejmngram.sejmometr.json.datamodel.klub2.KlubResponse;
import org.sejmngram.sejmometr.json.datamodel.wystapienie2.Data;
import org.sejmngram.sejmometr.json.datamodel.wystapienie2.WystapienieResponse;

public class SejmometrToCommonTransformer {

	private static final Logger LOG = Logger.getLogger(SejmometrToCommonTransformer.class.getName());
	
	private static Map<Integer, String> partyIdToPartyNameMap = new HashMap<Integer, String>();
	
	public static Wystapienie doTransform(WystapienieResponse sejmometrWystapienie)
			throws JsonParseException, JsonProcessingException, IOException {
		Data dane = sejmometrWystapienie.get_data();
		if (dane == null) {
			return null;
		}
		Wystapienie output = new Wystapienie();
		output.setData(dane.getData());
		output.setPartia(resolvePartyId(dane.getKlub_id()));
		output.setPosel(dane.getLudzie_nazwa());
		output.setStanowisko(dane.getStanowiska_nazwa());
		output.setTytul(dane.getTytul());
		output.setTresc(sejmometrWystapienie.get_layers().getHtml().getSejmWystapienia().getM_txt());
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
		String nazwa = sejmometrResponse.get_data().getNazwa();
		LOG.debug("Retrieved klub name: " + nazwa + " for klub id: " + klub_id);
		partyIdToPartyNameMap.put(partyId, nazwa);
		return nazwa;
	}
}
