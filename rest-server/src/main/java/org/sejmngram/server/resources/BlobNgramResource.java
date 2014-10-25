package org.sejmngram.server.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.connection.MySqlDbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;

@Path("/api/ngram")
@Produces(MediaType.APPLICATION_JSON)
public class BlobNgramResource {

	private final DbConnector db;
	
	public BlobNgramResource() {
        this.db = new MySqlDbConnector();
        this.db.connect();
	}
	
	@GET
	@Path("{ngram}")
    @Timed
	public NgramResponse sayHello(@PathParam("ngram") String ngramName,
			@QueryParam("name") Optional<String> name) {
		return db.retrieve(ngramName);
	}

}
