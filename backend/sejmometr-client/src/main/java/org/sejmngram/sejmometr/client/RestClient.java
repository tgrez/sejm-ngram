package org.sejmngram.sejmometr.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestClient {
	
	private static final String url = "http://sejmometr.pl/sejm_wystapienia/";
	
	private static final String acceptHeader = "application/vnd.EPF_API.v1+json";
	
	public static String getWystapienie(int id) {
		Client client = Client.create();
		WebResource webResource = client.resource(url + id);
		ClientResponse response = webResource.accept(acceptHeader).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		return response.getEntity(String.class);
	}
}
