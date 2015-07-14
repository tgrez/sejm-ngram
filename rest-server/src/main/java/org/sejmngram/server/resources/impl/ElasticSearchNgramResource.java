package org.sejmngram.server.resources.impl;

import io.dropwizard.elasticsearch.config.EsConfiguration;
import io.dropwizard.elasticsearch.managed.ManagedEsClient;

import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.elasticsearch.ElasticSearchConnector;
import org.sejmngram.server.config.SejmFilesConfiguration;
import org.sejmngram.server.resources.NgramResource;

@Path("/api/ngram")
@Produces(MediaType.APPLICATION_JSON)
public class ElasticSearchNgramResource implements NgramResource {

    private final DbConnector connector;

    public ElasticSearchNgramResource(ManagedEsClient managedEsClient, EsConfiguration esConfig,
            SejmFilesConfiguration sejmFilesConfig) {
        String indexName = esConfig.getSettings().get("index");
        // TODO searching phrases consisting of multiple words
        // TODO dbconnector decorator cache
        Set<String> dates = sejmFilesConfig.readDatesFromFile();
        this.connector = new ElasticSearchConnector(managedEsClient.getClient(), indexName, dates);
    }

    @Override
    public NgramResponse getNgram(String ngramName) {
        return connector.retrieve(ngramName);
    }

}
