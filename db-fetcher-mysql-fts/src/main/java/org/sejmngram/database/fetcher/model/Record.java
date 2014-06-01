package org.sejmngram.database.fetcher.model;

import java.util.Date;

/**
 * Created by michalsiemionczyk on 11/05/14.
 */
public class Record {
    private Date date;

    private int count;

    private int partyId;

    public Date getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }

    public int getPartyId() {
        return partyId;
    }


    public Record(Date date, int count, int partyId) {
        this.date = date;
        this.count = count;
        this.partyId = partyId;
    }
}
