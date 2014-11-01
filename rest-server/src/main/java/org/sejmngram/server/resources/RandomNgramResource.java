package org.sejmngram.server.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.math.RandomUtils;
import org.sejmngram.database.fetcher.json.datamodel.ListDate;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.PartiesNgrams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/api2/ngram")
@Produces(MediaType.APPLICATION_JSON)
public class RandomNgramResource {

	private static final Logger LOG = LoggerFactory.getLogger(RandomNgramResource.class);

	@GET
	@Path("{ngram}")
    @Timed
	public NgramResponse sayHello(@PathParam("ngram") String ngramName,
			@QueryParam("name") Optional<String> name) {
		LOG.error(ngramName);
		String ngramURLdecoded;
		try {
			ngramURLdecoded = URLDecoder.decode(ngramName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			ngramURLdecoded = ngramName;
		}
		return createRandomResponse(ngramURLdecoded);
	}
	
	private NgramResponse createRandomResponse(String ngramName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<PartiesNgrams> partiesNgrams = new ArrayList<PartiesNgrams>();

        for (int j = 0; j < RandomUtils.nextInt(9)+ 1 ; j++) {

            List<ListDate> listDates = new ArrayList<ListDate>();

            for (int i = 0; i < RandomUtils.nextInt(200); i++) {
                long beginDate = 689835600000l;
                listDates.add(new ListDate(sdf.format(new Date(beginDate + RandomUtils.nextInt())), RandomUtils.nextInt((200))));
            }

            partiesNgrams.add(new PartiesNgrams("Party" + j, listDates));
        }
        return new NgramResponse(ngramName, partiesNgrams);
	}

}
