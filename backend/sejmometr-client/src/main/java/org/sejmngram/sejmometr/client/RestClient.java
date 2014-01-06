package org.sejmngram.sejmometr.client;

import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.sejmometr.json.datamodel.Wystapienie;

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
			String output = response.getEntity(String.class);
			System.out.println("Output from Server:\n");
			System.out.println(output);
			System.out.println();
			System.out.println("Output after transformation: \n");
			System.out.println(JsonProcessor.transform(output, Wystapienie.class).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
