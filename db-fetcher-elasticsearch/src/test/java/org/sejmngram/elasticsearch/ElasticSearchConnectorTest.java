package org.sejmngram.elasticsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.junit.Before;
import org.junit.Test;
import org.sejmngram.database.fetcher.json.datamodel.ListDate;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.PartiesNgrams;

public class ElasticSearchConnectorTest {

    private static final String SEARCHED_NGRAM = "posel";
    private static final String INDEX = "sample";
    private static final String TERM_COUNT = "term_count";
    private static final String DATA = "data";
    private static final String PARTIA = "partia";

    private static final int TERM_COUNT_1 = 2;
    private static final int TERM_COUNT_2 = 7;
    private static final String DATE1 = "1997-05-22";
    private static final String DATE2 = "1997-05-23";

    private Client client = mock(Client.class);
    private ElasticSearchConnector connector = new ElasticSearchConnector(client, INDEX);
    private SearchHits searchHits = mock(SearchHits.class);

    @Before
    public void setup() {
        SearchResponse searchResponse = mockSearchResponse();
        when(searchResponse.getHits()).thenReturn(searchHits);
        SearchHit searchHitMock1 = mockSearchHit(TERM_COUNT_1, DATE1);
        SearchHit searchHitMock2 = mockSearchHit(TERM_COUNT_2, DATE2);
        setArrayForSearchHits(new SearchHit[] {searchHitMock1, searchHitMock2});
    }

    private void setArrayForSearchHits(SearchHit[] array) {
        when(searchHits.getHits()).thenReturn(array);
        when(searchHits.iterator()).thenReturn(Arrays.asList(array).iterator());
    }

    private SearchHit mockSearchHit(int term_count, String date) {
        SearchHit searchHitMock = mock(SearchHit.class);
        mockField(searchHitMock, TERM_COUNT, term_count);
        mockField(searchHitMock, PARTIA, "whatever");
        mockField(searchHitMock, DATA, date);
        return searchHitMock;
    }

    private void mockField(SearchHit searchHitMock, String fieldName,
            Object fieldValue) {
        SearchHitField fieldCount = mock(SearchHitField.class);
        when(searchHitMock.field(eq(fieldName))).thenReturn(fieldCount);
        when(fieldCount.getValue()).thenReturn(fieldValue);
    }

    private SearchResponse mockSearchResponse() {
        SearchRequestBuilder requestBuilderMock = mock(SearchRequestBuilder.class);
        when(client.prepareSearch(anyString())).thenReturn(requestBuilderMock);
        when(requestBuilderMock.setQuery(isA(QueryBuilder.class))).thenReturn(requestBuilderMock);
        when(requestBuilderMock.setSize(anyInt())).thenReturn(requestBuilderMock);
        when(requestBuilderMock.setTypes(anyString())).thenReturn(requestBuilderMock);
        when(requestBuilderMock.addField(anyString())).thenReturn(requestBuilderMock);
        when(requestBuilderMock.addScriptField(anyString(), anyString())).thenReturn(requestBuilderMock);
        SearchResponse searchResponse = mock(SearchResponse.class);
        when(requestBuilderMock.get()).thenReturn(searchResponse);
        return searchResponse;
    }

    @Test
    public void responseShouldContainTheSameNgramAsRequested() {
        NgramResponse ngramResponse = connector.retrieve("jakisNgram");

        assertEquals("jakisNgram".toLowerCase(), ngramResponse.getNgram());
    }

    @Test
    public void shouldReturnEmptyResponseWhenRetrievingEmptyNgram() {
        setArrayForSearchHits(new SearchHit[0]);
        NgramResponse ngramResponse = connector.retrieve("");

        assertTrue(ngramResponse.getPartiesNgrams().isEmpty());
    }

    @Test
    public void shouldCountOccurancesForGivenNgram() {
        NgramResponse ngramResponse = connector.retrieve(SEARCHED_NGRAM);

        assertEquals(TERM_COUNT_1 + TERM_COUNT_2, getNgramTotalCount(ngramResponse));
    }

    @Test
    public void shouldCountOccurancesForDifferentDates() {
        NgramResponse ngramResponse = connector.retrieve(SEARCHED_NGRAM);
        List<ListDate> listDates = ngramResponse.getPartiesNgrams().get(0)
                .getListDates();

        assertEquals(DATE1, listDates.get(0).getDate());
        assertEquals(DATE2, listDates.get(1).getDate());
        assertEquals(TERM_COUNT_1, listDates.get(0).getCount().intValue());
        assertEquals(TERM_COUNT_2, listDates.get(1).getCount().intValue());
    }

    @Test
    public void shouldReturnResponseWithAllDatesProvided() {
        Set<String> providedDates = new HashSet<String>(
                Arrays.asList(DATE1, DATE2, "1995-03-14", "1997-08-11", "2001-03-04"));
        connector = new ElasticSearchConnector(client, INDEX, providedDates);
        NgramResponse ngramResponse = connector.retrieve(SEARCHED_NGRAM);

        Set<String> receivedDates = new HashSet<String>();
        for (ListDate listDate : ngramResponse.getPartiesNgrams().get(0).getListDates()) {
            receivedDates.add(listDate.getDate());
        }

        assertEquals(providedDates.size(), receivedDates.size());
        assertTrue(receivedDates.containsAll(providedDates));
    }

    private int getNgramTotalCount(NgramResponse ngramResponse) {
        int totalCount = 0;
        for (PartiesNgrams partiesNgrams : ngramResponse.getPartiesNgrams()) {
            for (ListDate listDate : partiesNgrams.getListDates()) {
                totalCount += listDate.getCount();
            }
        }
        return totalCount;
    }
}
