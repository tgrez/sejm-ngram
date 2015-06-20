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

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchConnector.class);

    private static final int RESULT_SIZE = 1000 * 1000;
    private static final String TERM_COUNT = "term_count";
    private static final String TEXT_FIELD = "tresc";

    private static final String PARTIA = "partia";
    private static final String DATA = "data";

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
        SearchResponse esSearchResponse = queryElasticSearch(ngram);

        if (isContainingAnyResults(esSearchResponse)) {
            return createResponse(ngram, esSearchResponse);
        } else {
            return emptyResponse(ngram);
        }
    }

    private NgramResponse createResponse(String ngram, SearchResponse esSearchResponse) {
        ResponseBuilder responseBuilder = new ResponseBuilder(ngram, dates);
        if (esSearchResponse.getHits().getHits().length >= RESULT_SIZE) {
            LOG.warn("Maximum number of elasticsearch search hits reached: " + RESULT_SIZE
                    + ". Possibly not receiving all available results from elasticsearch.");
        }
        for (SearchHit searchHit : esSearchResponse.getHits()) {
            int termCount = searchHit.field(TERM_COUNT).getValue();
            String partyName = searchHit.field(PARTIA).getValue();
            String date = searchHit.field(DATA).getValue();
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
        QueryBuilder query = QueryBuilders.queryString(ngram).defaultField(TEXT_FIELD);
        SearchResponse searchResponse = client.prepareSearch(index).setQuery(query).setSize(RESULT_SIZE)
                .addScriptField(TERM_COUNT, createCountScript(ngram))
                .addField(DATA)
                .addField(PARTIA)
                .addField(TEXT_FIELD)
                .addField("id")
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
