package org.sejmngram.elasticsearch;

import static org.sejmngram.database.fetcher.json.datamodel.ResponseBuilder.emptyResponse;

import java.util.*;

import org.apache.commons.lang.NotImplementedException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.sejmngram.database.fetcher.connection.NgramDbConnector;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchConnector extends AbstractElasticSearchConnector implements NgramDbConnector {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchConnector.class);

    private static final int RESULT_SIZE_LIMIT = 1000 * 1000;

    private final String index;
    private Map<String, String> partiesIdMap;
    private Set<String> dates = new HashSet<String>();

    public ElasticSearchConnector(Client client, String index) {
    	super(client);
        this.index = index;
        this.partiesIdMap = new HashMap<String, String>();
    }

    public ElasticSearchConnector(Client client, String index,
            Set<String> providedDates, Map<String, String> partiesIdMap) {
        this(client, index);
        this.partiesIdMap = partiesIdMap;
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
            String partyName = lookupPartyName(searchHit);
            String date = searchHit.field(DATE_FIELD).getValue();
            responseBuilder.addOccurances(partyName, date, termCount);
        }
        return responseBuilder.generateResponse();
    }

    private String lookupPartyName(SearchHit searchHit) {
        String partyIdString = searchHit.field(PARTY_FIELD).getValue();
        String partyName = partiesIdMap.get(partyIdString);
        if (partyName == null)
            return partyIdString;
        else return partyName;
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

	@Override
	protected SearchRequestBuilder buildQuery(SearchRequestBuilder searchRequestBuilder, String phrase) {
		return searchRequestBuilder
		      .setSize(RESULT_SIZE_LIMIT)
		      .addScriptField(TERM_COUNT, createCountScript(phrase))
		      .addField(DATE_FIELD)
		      .addField(PARTY_FIELD)
		      .addField(TEXT_FIELD)
		      .addField(ID_FIELD);
	}

	@Override
	protected String getIndex() {
		return index;
	}

}
