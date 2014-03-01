package org.sejmngram.dbinserter;

/**
 * Created by michalsiemionczyk on 01/03/14.
 */
public class RowData {

    private String blob = "";

    private int nrEntries;


    public RowData(){
        blob = "";
    }

    public String getBlob() {
        return blob;
    }

    public int getNrEntries() {
        return nrEntries;
    }

    public void inreaseNrEntries(){
        nrEntries++;
    }

    public void setBlob(String blob) {
        this.blob = blob;
    }

    public void setNrEntries(int nrEntries) {
        this.nrEntries = nrEntries;
    }



}
