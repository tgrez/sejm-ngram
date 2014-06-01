package org.sejmngram.server.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.server.factory.NgramProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;

import com.example.helloworld.resources.DemoNgramResource;
import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;

@Path("/api2/ngram")
@Produces(MediaType.APPLICATION_JSON)
public class DemoNgramResource {

	private static final LoggerContext factory = (LoggerContext) LoggerFactory.getILoggerFactory();
	private static final Logger root = factory.getLogger(DemoNgramResource.class);
	
	private final NgramProvider ngramProvider;

	public DemoNgramResource() {
		this.ngramProvider = new NgramProvider();
	}
	
	@GET
	@Path("{ngram}")
    @Timed
	public NgramResponse sayHello(@PathParam("ngram") String ngramName,
			@QueryParam("name") Optional<String> name) {
		root.error(ngramName);
		String ngramURLdecoded;
		try {
			ngramURLdecoded = URLDecoder.decode(ngramName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			ngramURLdecoded = ngramName;
		}
		return ngramProvider.generateNgramResponse(ngramURLdecoded);
	}


}
