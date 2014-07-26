package org.sejmngram.database.fetcher.json.datamodel;

import java.util.Date;

/**
 * Created by michalsiemionczyk on 26/07/14.
 */
public class Occurence {
    Date date;
    int partyId;
    int deputyId;
    String text;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public int getDeputyId() {
        return deputyId;
    }

    public void setDeputyId(int deputyId) {
        this.deputyId = deputyId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public Occurence(Date date, int partyId, int deputyId, String text) {
        this.date = date;
        this.partyId = partyId;
        this.deputyId = deputyId;
        this.text = text;
    }

}
