package org.sejmngram.dbinserter.model;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
        private byte[] blob;
        private int nrEntries;

        public Row() {
            this.blob = new byte[]{};
        }

        public byte[] getBlob() {
            if (blob == null)
                return null;
            byte[] result = new byte[blob.length];
            System.arraycopy(blob, 0, result, 0, blob.length);
            return result;
        }

        public int getNrEntries() {
            return nrEntries;
        }

        public void inreaseNrEntries() {
            nrEntries++;
        }

        public void setBlob(byte[] blob) {
            if (blob == null) {
                this.blob = null;
                return;
            }
            this.blob = ArrayUtils.addAll(this.blob, blob);
        }

        public void setNrEntries(int nrEntries) {
            this.nrEntries = nrEntries;
        }
    }

    public RowData(){
        blobs = new ArrayList<Row>();
        blobs.add( new Row());
    }

    private ByteBuffer buffer = ByteBuffer.allocate(16);

    public void addEntryToBlob(String posixTimestamp, int poselId, int partiaId) {

        if (getNrEntriesInLastBlob() == MAX_BLOB_ENTRIES) {
            this.blobs.add(new Row());
        }

        buffer.put(posixTimestamp.getBytes(StandardCharsets.UTF_8), 0, 8);
        buffer.putInt(8, poselId);
        buffer.putInt(12, partiaId);

        getLastRow().inreaseNrEntries();
        getLastRow().setBlob(buffer.array());
        buffer.clear();
    }

    public ArrayList<Row> getAllRows(){
        return  this.blobs;
    }

    public byte[] getLastBlob() {
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
