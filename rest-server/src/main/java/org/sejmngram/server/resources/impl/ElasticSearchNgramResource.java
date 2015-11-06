package org.sejmngram.server.resources.impl;

import io.dropwizard.elasticsearch.config.EsConfiguration;
import io.dropwizard.elasticsearch.managed.ManagedEsClient;

import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.sejmngram.database.fetcher.connection.NgramDbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.elasticsearch.ElasticSearchConnector;
import org.sejmngram.server.config.SejmFilesConfiguration;
import org.sejmngram.server.resources.NgramResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/ngram")
@Produces(MediaType.APPLICATION_JSON)
public class ElasticSearchNgramResource implements NgramResource {

    private static final Logger LOG = LoggerFactory
            .getLogger(ElasticSearchNgramResource.class);

    private final NgramDbConnector connector;

    public ElasticSearchNgramResource(ManagedEsClient managedEsClient, EsConfiguration esConfig,
            SejmFilesConfiguration sejmFilesConfig) {
        String indexName = esConfig.getSettings().get("index");
        // TODO searching phrases consisting of multiple words
        // TODO dbconnector decorator cache
        Set<String> dates = sejmFilesConfig.readDatesFromFile();
        Map<String, String> partyIdNameMap = sejmFilesConfig.returnPartiesIdMapFromFile();
        this.connector = new ElasticSearchConnector(managedEsClient.getClient(), indexName, dates, partyIdNameMap);
    }

    @Override
    public NgramResponse getNgram(String ngramName) {
        return connector.retrieve(ngramName);
    }

}
