package org.sejmngram.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;

public class ElasticsearchIntegrationTest {

    @Test
    public void shouldSuccessfullyGetTermCountFromElasticsearch() {
        Client client = new TransportClient()
            .addTransportAddress(new InetSocketTransportAddress("127.0.0.1",9300));
 
        QueryBuilder query = QueryBuilders.queryString("java")
            .defaultField("text");
 
        SearchResponse sr = client
            .prepareSearch("sample")
            .setQuery(query)
            .addScriptField("term_count", "_index['text']['java'].tf()")
            .execute()
            .actionGet();
 
        long hits = sr.getHits().getTotalHits();
 
        System.out.println("hits:["+hits+"]");
        for(SearchHit hit : sr.getHits()){
            System.out.println("--------------------");
            System.out.println("term_count:"+hit.field("term_count").getValue());
        }
 
        client.close();
    }

}
