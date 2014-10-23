package org.sejmngram.database.fetcher.json.datamodel;

import java.util.ArrayList;
import java.util.List;

public class PartiesNgrams {

    private String name;
    private List<ListDate> listDates = new ArrayList<ListDate>();
    
	/**
	 * Needed by Jackson
	 */
    public PartiesNgrams() {}

    public PartiesNgrams(String name, List<ListDate> listDates) {
    	this.name = name;
    	this.listDates = listDates;
    }
    
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<ListDate> getListDates() {
		return listDates;
	}

	public void setListDates(List<ListDate> listDates) {
		this.listDates = listDates;
	}
}
