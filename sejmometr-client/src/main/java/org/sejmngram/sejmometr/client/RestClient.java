package org.sejmngram.sejmometr.client;

import org.apache.log4j.Logger;
import org.sejmngram.common.Configuration;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestClient {
	
	private static final Logger LOG = Logger.getLogger(RestClient.class.getName());
	
	private static final String acceptHeader = Configuration.getInstance().getSejmometrAcceptHeader(); 
	private static final String url = Configuration.getInstance().getSejmometrUrl();
	private static final String urlEnding = Configuration.getInstance().getSejmometrUrlEnding();
	private final Client client = Client.create();
	private final String path;
	
	public RestClient(String path) {
		this.path = path;
	}
	
	public String get(int id) {
		String completeUrl = url + path + id + urlEnding;
		WebResource webResource = client.resource(completeUrl);
		LOG.debug("Sent request to: " + completeUrl);
		ClientResponse response = webResource.accept(acceptHeader).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		LOG.debug("Recieved response from: " + completeUrl);
		return response.getEntity(String.class);
	}
}
