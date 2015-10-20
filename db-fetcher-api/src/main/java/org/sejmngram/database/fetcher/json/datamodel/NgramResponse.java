package org.sejmngram.database.fetcher.json.datamodel;

import java.util.ArrayList;
import java.util.List;

public class NgramResponse {

    private String ngram;
    private List<PartiesNgrams> partiesNgrams = new ArrayList<PartiesNgrams>();

    /**
     * Needed by Jackson
     */
    public NgramResponse() {
    }

    public NgramResponse(String ngram) {
        this.ngram = ngram;
    }

    public NgramResponse(String ngram, List<PartiesNgrams> partiesNgrams) {
        this.ngram = ngram;
        this.partiesNgrams = partiesNgrams;
    }

    public List<PartiesNgrams> getPartiesNgrams() {
        return partiesNgrams;
    }

    public void setPartiesNgrams(List<PartiesNgrams> partiesNgrams) {
        this.partiesNgrams = partiesNgrams;
    }

    public void setNgram(String ngram) {
        this.ngram = ngram;
    }

    public String getNgram() {
        return ngram;
    }
}
