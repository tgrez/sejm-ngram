package org.sejmngram.sejmometr.transform;

import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.sejmometr.json.datamodel.Data;
import org.sejmngram.sejmometr.json.datamodel.SejmometrWystapienie;

public class SejmometrToCommonTransformer {

	public static Wystapienie doTransform(SejmometrWystapienie sejmometrWystapienie) {
		Data dane = sejmometrWystapienie.getDocument().getContent().getData();
		if (dane == null)
			return null;
		Wystapienie output = new Wystapienie();
		output.setData(dane.getData());
		// TODO set party name instead of klub_id
		// TODO set party for the representative as it was at the time of the speech
		output.setPartia(dane.getKlub_id());
		output.setPosel(dane.getLudzie_nazwa());
		output.setStanowisko(dane.getStanowiska_nazwa());
		output.setTytul(dane.getTytul());
		output.setTresc(sejmometrWystapienie.getDocument().getContent()
				.getLayers().getHtmlText());
		return output;
	}
}
