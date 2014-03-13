package org.sejmngram.dbinserter.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by michalsiemionczyk on 01/03/14.
 */
public class RowData {

    public final static int MAX_BLOB_ENTRIES = 1000;

    public static final char BLOB_ENTRY_WORD_SEPARATOR = '%';
    public static final char BLOB_ENTRIES_SEPARATOR = '^';

    /** Each of these blobs is meant to contain max #MAX_BLOB_ENTRIES entries  */
    ArrayList<Row> blobs;


    /** Date from */
    private Date dateFrom;

    private Date dateTo;



    public static class Row {
        private String blob;
        private int nrEntries;

        public Row(){
            this.blob = "";
        }

        public String getBlob() { return this.blob;};

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




    public RowData(){
        blobs = new ArrayList<Row>();
        blobs.add( new Row());
    }



        /** Add entry to blob and increases nr entries
         * Handles creation of new blob when needed
         * */

    private final static int MAX_PARTIES = 10;
    private final static int MAX_POSLY = 400;



     public void addEntryToBlob(long posixTimestamp, String posel, String partia, boolean randomizeIds) {
        if ( getNrEntriesInLastBlob() == MAX_BLOB_ENTRIES){
            this.blobs.add( new Row());
        }




        StringBuffer sb = new StringBuffer( getLastBlob() );

        if ( randomizeIds ){
            // THIS IS FOR TESTING ONLY
            sb.append( posixTimestamp ).append( BLOB_ENTRY_WORD_SEPARATOR)
                    .append(          (int)   (Math.random() * MAX_POSLY ))
                    .append(BLOB_ENTRY_WORD_SEPARATOR)
                    .append( (int) ( Math.random() * MAX_PARTIES )).append(BLOB_ENTRIES_SEPARATOR);

        } else {
            // THIS IS GNERATING TEXT FOR POSEL / PARTIA
            //add timestamp
            sb.append( posixTimestamp ).append( BLOB_ENTRY_WORD_SEPARATOR)
                    .append( posel ).append( BLOB_ENTRY_WORD_SEPARATOR)
                    .append( partia ).append( BLOB_ENTRIES_SEPARATOR);
        }





        getLastRow().inreaseNrEntries();
        getLastRow().setBlob( sb.toString());
    }

    public ArrayList<Row> getAllRows(){
        return  this.blobs;
    }

    public String getLastBlob() {
        return getLastRow().getBlob();
    }

    public int getNrEntriesInLastBlob() {
        return getLastRow().getNrEntries();
    }

    public int getNrAllEntries(){
        int nr = 0;

        for ( Row r : this.blobs){
            nr += r.getNrEntries();
        }

        return nr;
    }

    public void inreaseNrEntries(){

        //check whether nr enties in not lo
        if ( getNrEntriesInLastBlob() < MAX_BLOB_ENTRIES){
            getLastRow().inreaseNrEntries();
        } else {

        }

    }


    public Date getDateFrom() {
        return dateFrom;
    }


    public Date getDateTo() {
        return dateTo;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    private Row getLastRow(){
        return this.blobs.get( this.blobs.size() - 1);
    }


}
