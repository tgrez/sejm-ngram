package org.sejmngram.database.fetcher.model;

import java.util.Date;

/**
 * Created by michalsiemionczyk on 11/05/14.
 */
public class Record {
    private Date date;

    private int count;

    public Date getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }


    public Record(Date date, int count) {
        this.date = date;
        this.count = count;
    }
}
