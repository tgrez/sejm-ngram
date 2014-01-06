package org.sejmngram.sejmometr.client;

import java.util.ArrayList;
import java.util.List;

import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Dokument;
import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.common.json.print.Printer;
import org.sejmngram.sejmometr.json.datamodel.Data;
import org.sejmngram.sejmometr.json.datamodel.SejmometrWystapienie;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestClient {
	public static void main(String[] args) {
		try {
			Client client = Client.create();
			WebResource webResource = client.resource("http://sejmometr.pl/sejm_wystapienia/10");
			ClientResponse response = webResource.accept("application/vnd.EPF_API.v1+json").get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			String responseString = response.getEntity(String.class);
			System.out.println("### RESPONSE FROM SERVER ###");
			System.out.println();
			System.out.println(responseString);
			System.out.println();
			System.out.println("### OUTPUT AFTER TRANSFORMATION ###");
			System.out.println();
			SejmometrWystapienie sejmometrWystapienie = JsonProcessor.transform(responseString, SejmometrWystapienie.class);
			System.out.println(sejmometrWystapienie);
			Data dane = sejmometrWystapienie.getDocument().getContent().getData();
			Wystapienie output = new Wystapienie();
			output.setData(dane.getData());
			output.setPartia(dane.getKlub_id());
			output.setPosel(dane.getLudzie_nazwa());
			output.setStanowisko(dane.getStanowiska_nazwa());
			output.setTytul(dane.getTytul());
			output.setTresc(sejmometrWystapienie.getDocument().getContent().getLayers().getHtmlText());
			System.out.println();
			System.out.println("### INTERMEDIATE COMMON OUTPUT ###");
			System.out.println();
			System.out.println(output);
			Dokument dokument = new Dokument();
			List<Wystapienie> list = new ArrayList<Wystapienie>();
			list.add(output);
			dokument.setWystapienia(list);
			Printer.printCommonJsonToFile(dokument);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
