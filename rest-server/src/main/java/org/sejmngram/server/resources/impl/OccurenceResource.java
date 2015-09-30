package org.sejmngram.server.resources.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

@Path("/api/occurence")
@Produces(MediaType.APPLICATION_JSON)
public class OccurenceResource {

	@GET
    @Path("{id}")
    @Timed
    public String getOccurence(@PathParam("id") String id) throws MalformedURLException {
		//TODO odpytać elasticsearch o wystapienie "id" i zwrocic jako string
		//calosc odpowiedzi.
		// Wykorzystac interfejs RESTowy elasticsearcha
		// np. curl 'http://localhost:9200/sejmngram/wystapienie/53019961108'
		// do wysyłania zapytania użyć zewnętrznej biblioteki albo po prostu po javovym http
		
		URL elasticSearchUrl = new URL("http://localhost:9200/sejmngram/occurence/" + id);
		HttpURLConnection elasticSearchConnection = null;
		InputStream response = null;
		
		try {
			elasticSearchConnection = (HttpURLConnection)elasticSearchUrl.openConnection();
			elasticSearchConnection.setRequestMethod("GET");

		    response = elasticSearchConnection.getInputStream();
		    
		    InputStreamReader is = new InputStreamReader(response);
		    StringBuilder sb=new StringBuilder();
		    BufferedReader br = new BufferedReader(is);
		    String read = br.readLine();

		    while(read != null) {
		        //System.out.println(read);
		        sb.append(read);
		        read =br.readLine();

		    }
		    
		    String str = sb.toString();
		    return str;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

