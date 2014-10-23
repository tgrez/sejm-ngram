package org.sejmngram.database.fetcher.json.datamodel;


public class ListDate {

    private String date;
    private Integer count;

	/**
	 * Needed by Jackson
	 */
    public ListDate() {}

    public ListDate(String date, Integer count) {
    	this.date = date;
    	this.count = count;
    }
    
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
