package org.sejmngram.elasticsearch;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractElasticSearchConnector {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractElasticSearchConnector.class);
	
	protected static final String ID_FIELD = "id";
    protected static final String TERM_COUNT = "term_count";
    protected static final String TEXT_FIELD = "tresc";
    protected static final String PARTY_FIELD = "partia";
    protected static final String DATE_FIELD = "data";
    
	protected Client client;
	
	public AbstractElasticSearchConnector(Client client) {
		this.client = client;
	}
	
    protected SearchResponse queryElasticSearch(String phrase) {
        QueryBuilder query = QueryBuilders.matchPhraseQuery(TEXT_FIELD, phrase);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(getIndex())
                .setQuery(query);
        LOG.trace("ElasticSearch Query using Java Client API:\n" + searchRequestBuilder.internalBuilder());

        SearchRequestBuilder searchRequestBuilder2 = buildQuery(searchRequestBuilder, phrase);
        return searchRequestBuilder2.get();
    }
    protected abstract SearchRequestBuilder buildQuery(SearchRequestBuilder searchRequestBuilder, String phrase);

	protected abstract String getIndex();

}
