package org.sejmngram.elasticsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Before;
import org.junit.Test;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

public class ElasticsearchIntegrationTest {

    @Before
    public void insertData() throws IOException {
        String hostAddress = "http://localhost:9200/";
        sendPutRequest(hostAddress + "sample/doc_count/a", "{\"text\": \"Java, Java, Java, Java is great.\"}");
        sendPutRequest(hostAddress + "sample/doc_count/b", "{\"text\": \"Java, Java is coffee.\"}");
    }

    private void sendPutRequest(String address, String content)
            throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(address);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        OutputStreamWriter out = new OutputStreamWriter(
            httpCon.getOutputStream());
        out.write(content);
        out.close();
        httpCon.getInputStream();
    }

    @Test
    public void shouldSuccessfullyGetTermCountFromElasticsearch() {
        Client client = new TransportClient()
            .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));

        String queryTerm = "java";
 
        QueryBuilder query = QueryBuilders.queryString(queryTerm)
            .defaultField("text");
 
        SearchResponse sr = client
            .prepareSearch("sample")
            .setQuery(query)
            .addScriptField("term_count", "_index['text']['" + queryTerm + "'].tf()")
            .execute()
            .actionGet();
 
        long hits = sr.getHits().getTotalHits();
        assertEquals(2, hits);
 
        System.out.println("hits:["+hits+"]");
        for(SearchHit hit : sr.getHits()){
            System.out.println("--------------------");
            System.out.println("term_count:"+hit.field("term_count").getValue());
        }

        client.close();
    }

    @Test
    public void connectToRealElasticSearchInstanceByConnector() throws Exception {
        Client client = new TransportClient()
            .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
        ElasticSearchConnector esConnector = new ElasticSearchConnector(client, "sejmngram");

        NgramResponse ngramResponse = esConnector.retrieve("Argentyna");

        assertNotNull(ngramResponse);
        assertTrue(ngramResponse.getPartiesNgrams().size() > 0);
    }

    @Test
    public void searchPhraseWithMultipleWords() {
        Client client = new TransportClient()
            .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
        ElasticSearchConnector esConnector = new ElasticSearchConnector(client, "sejmngram");

        NgramResponse ngramResponse = esConnector.retrieve("wysoka izbo");

        assertNotNull(ngramResponse);
        assertTrue(ngramResponse.getPartiesNgrams().size() > 0);
    }
}
