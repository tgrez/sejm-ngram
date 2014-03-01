package org.sejmngram.database.fetcher.json.datamodel;


public class ListDate {

    private final String date;
    private final Integer count;
    
    public ListDate(String date, Integer count) {
    	this.date = date;
    	this.count = count;
    }
    
	public Integer getCount() {
		return count;
	}

	public String getDate() {
		return date;
	}
}
