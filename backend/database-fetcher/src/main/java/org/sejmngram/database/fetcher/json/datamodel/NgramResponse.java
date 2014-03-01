package org.sejmngram.database.fetcher.json.datamodel;

import java.util.ArrayList;
import java.util.List;

public class NgramResponse {

	private final String ngram;
    private List<PartiesNgrams> partiesNgrams = new ArrayList<PartiesNgrams>();
    
    public NgramResponse(String ngram, List<PartiesNgrams> partiesNgrams) {
    	this.ngram = ngram;
    	this.partiesNgrams = partiesNgrams;
    }
    
    public List<PartiesNgrams> getPartiesNgrams() {
    	return partiesNgrams;
    }

	public String getNgram() {
		return ngram;
	}
}
