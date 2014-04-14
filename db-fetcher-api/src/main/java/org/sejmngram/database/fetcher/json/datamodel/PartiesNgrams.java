package org.sejmngram.database.fetcher.json.datamodel;

import java.util.ArrayList;
import java.util.List;

public class PartiesNgrams {

    private final String name;
    private List<ListDate> listDates = new ArrayList<ListDate>();
    
    public PartiesNgrams(String name, List<ListDate> listDates) {
    	this.name = name;
    	this.listDates = listDates;
    }
    
	public String getName() {
		return name;
	}
	
	public List<ListDate> getListDates() {
		return listDates;
	}
}
