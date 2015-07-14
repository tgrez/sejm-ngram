package org.sejmngram.elasticsearch;

import static org.sejmngram.database.fetcher.json.datamodel.ResponseBuilder.emptyResponse;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.sejmngram.database.fetcher.connection.DbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchConnector implements DbConnector {

    private static final String ID_FIELD = "id";

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchConnector.class);

    private static final int RESULT_SIZE_LIMIT = 1000 * 1000;
    private static final String TERM_COUNT = "term_count";
    private static final String TEXT_FIELD = "tresc";

    private static final String PARTY_FIELD = "partia";
    private static final String DATE_FIELD = "data";

    private final String index;
    private Client client;
    private Set<String> dates = new HashSet<String>();

    public ElasticSearchConnector(Client client, String index) {
        this.client = client;
        this.index = index;
    }

    public ElasticSearchConnector(Client client, String index,
            Set<String> providedDates) {
        this(client, index);
        this.dates = Collections.unmodifiableSet(providedDates);
    }

    @Override
    public NgramResponse retrieve(String ngram) {
        // TODO sanitize input
        ngram = normalizeNgram(ngram);
        LOG.trace("Received ngram request: " + ngram);
        LOG.trace("Querying ElasticSearch...");
        SearchResponse esSearchResponse = queryElasticSearch(ngram);
        LOG.trace("ElasticSearch response received");

        NgramResponse ngramResponse;
        if (isContainingAnyResults(esSearchResponse)) {
            LOG.trace("There are " + esSearchResponse.getHits().getTotalHits() + " search hits");
            ngramResponse = createResponse(ngram, esSearchResponse);
        } else {
            LOG.trace("Does not contain any results, returning empty response");
            ngramResponse = emptyResponse(ngram, dates);
        }
        LOG.trace("Finished processing.");
        return ngramResponse;
    }

    private NgramResponse createResponse(String ngram, SearchResponse esSearchResponse) {
        ResponseBuilder responseBuilder = new ResponseBuilder(ngram, dates);
        // TODO histogram result size? https://dropwizard.github.io/metrics/3.1.0/manual/core/#histograms
        if (esSearchResponse.getHits().getTotalHits() >= RESULT_SIZE_LIMIT) {
            LOG.warn("Maximum number of elasticsearch search hits reached: " + RESULT_SIZE_LIMIT
                    + ". Possibly not receiving all available results from elasticsearch.");
        }
        for (SearchHit searchHit : esSearchResponse.getHits()) {
            int termCount = searchHit.field(TERM_COUNT).getValue();
            String partyName = searchHit.field(PARTY_FIELD).getValue();
            String date = searchHit.field(DATE_FIELD).getValue();
            responseBuilder.addOccurances(partyName, date, termCount);
        }
        return responseBuilder.generateResponse();
    }

    private String normalizeNgram(String ngram) {
        return ngram != null ? ngram.toLowerCase() : null;
    }

    private boolean isContainingAnyResults(SearchResponse searchResponse) {
        return searchResponse != null
                && searchResponse.getHits() != null
                && searchResponse.getHits().getHits() != null
                && searchResponse.getHits().getHits().length > 0;
    }

    private SearchResponse queryElasticSearch(String ngram) {
        QueryBuilder query = QueryBuilders.matchPhraseQuery(TEXT_FIELD, ngram);
        SearchResponse searchResponse = client.prepareSearch(index)
                .setQuery(query).setSize(RESULT_SIZE_LIMIT)
                .addScriptField(TERM_COUNT, createCountScript(ngram))
                .addField(DATE_FIELD)
                .addField(PARTY_FIELD)
                .addField(TEXT_FIELD)
                .addField(ID_FIELD)
                .get();
        return searchResponse;
    }

    private String createCountScript(String ngram) {
        return "_index['" + TEXT_FIELD + "']['" + ngram + "'].tf()";
    }

    @Override
    public NgramResponse retrieveByParty(String ngram, int partyId) {
        throw new NotImplementedException();
    }

    @Override
    public NgramResponse retrieveByPosel(String ngram, int poselId) {
        throw new NotImplementedException();
    }

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {
        client.close();
    }

}
