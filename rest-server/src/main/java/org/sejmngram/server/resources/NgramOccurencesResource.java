package org.sejmngram.server.resources;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import org.sejmngram.database.fetcher.json.datamodel.NgramOccurencesResponse;
import org.sejmngram.server.factory.NgramOccurencesProvider;
import org.skife.jdbi.v2.DBI;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * Call me with http://localhost:8080/service/api/occurences?ngram=piwo?date=123?limitPerPage=2?nrPageRequested=2
 */
@Path("/api/occurences")
@Produces(MediaType.APPLICATION_JSON)
public class NgramOccurencesResource {

	private final NgramOccurencesProvider ngramOccurencesProvider;

	public NgramOccurencesResource(DBI jdbi, String partyFilename, String poselFilename) {
		this.ngramOccurencesProvider = new NgramOccurencesProvider(jdbi, partyFilename, poselFilename);
	}
	
	@GET
    @Timed
	public NgramOccurencesResponse getResponse(
			@QueryParam("ngram") String ngramName, @QueryParam("date") Date date,
            @QueryParam("limitPerPage") Integer limitPerPage,
            @QueryParam("nrPageRequested") Integer nrPageRequested) {
        return NgramOccurencesResponse.getFakeOne(); //TODO don't use fake one
//        return ngramOccurencesProvider.generateNgramOccurenceResponse(ngramName.get(), date.get(),
//                limitPerPage.get(), nrPageRequested.get());
    }
}
