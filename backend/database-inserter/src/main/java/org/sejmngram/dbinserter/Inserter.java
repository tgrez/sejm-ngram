package org.sejmngram.dbinserter;

import com.sun.rowset.internal.Row;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Dokument;
import org.sejmngram.common.json.datamodel.Wystapienie;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by michalsiemionczyk on 14/02/14.
 */
public class Inserter {

    public static final char BLOB_ENTRY_WORD_SEPARATOR = '%';
    public static final char BLOB_ENTRIES_SEPARATOR = '^';


    public static String db_host = "localhost";
    static String db_base = "sejmngram";
    static String db_user = "root";
    static String db_pass = "";
    static String db_select_query = "select * from ngrams";



    public static void main (String[] args){

        Inserter ins = new Inserter();
//        ins.test();


        //read dokuemnt
        Dokument d = ins.getDokumentFromJsonFile();

        //insert to DB

        ins.insertDokumentToDB(d);

        System.out.println( " Hello world!");
    }

    private void initDB(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private Connection getConnection(){
        String url = "jdbc:mysql://" + db_host  + "/" + db_base;
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, db_user, db_pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return con;
    }

    private void insertDokumentToDB( Dokument d ){

//        unixtimestamp(seconds)%Cezary Grarczyk%PlatformaObywatelska^unixtimestamp%AleksanderOlechowski%PlatformaObywatelska^

        HashMap<String, RowData> blobsMap = new HashMap<String, RowData>();

        int i = 0 ;
        int nrAllWYstapienie = d.getWystapienia().size();
        for ( Wystapienie wyst : d.getWystapienia()){

            System.out.println("processing wystapienie " + i + " of " + nrAllWYstapienie);

            String[] words = wyst.getTresc().split(" ");
            long unixPosixTimestamp = wyst.getData().getTime() / 1000;
            String posel = wyst.getPosel();
            String partia = wyst.getPartia();

            for ( String word : words ){

                RowData rowData = null;

                //check it word exists
                if ( blobsMap.containsKey( word )){
                    rowData = blobsMap.get( word );
                } else {
                    rowData = new RowData();
                }

                StringBuffer sb = new StringBuffer( rowData.getBlob() );
                //add timestamp
                sb.append( unixPosixTimestamp ).append( BLOB_ENTRY_WORD_SEPARATOR)
                  .append( posel ).append( BLOB_ENTRY_WORD_SEPARATOR)
                  .append( partia ).append( BLOB_ENTRIES_SEPARATOR);

                rowData.inreaseNrEntries();
                rowData.setBlob( sb.toString() );

                //save blob to hashmap
                blobsMap.put( word, rowData );
            }
         i++;
        }

        System.out.println( "nr different words:" + blobsMap.keySet().size());
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        String keyMin = "";
        String keyMax = "";
        for ( String key :  blobsMap.keySet() ){
            int keySize = blobsMap.get( key ).getNrEntries();
            if ( keySize > max ){
                max = keySize;
                keyMax = key;
            }
            if ( keySize < min ){
                min = keySize;
                keyMin = key;
            }

        }

        System.out.println( "min nrOccurences" + min + " for : " + keyMin);
        System.out.println( "max  nrOccurences" + max + " for: "+ keyMax );




    }

    private Dokument getDokumentFromJsonFile(){

//        File fA = new File("./scripts");
//        for (File fL : fA.listFiles()){
//            Logger.getAnonymousLogger().info( fL.getPath());
//        }

        //for maven build use ".."
        File f = new File( "./scripts/sejmometr/downloadedData/2011-11-18.json");

        Dokument d = null;
        try {
            d = JsonProcessor.transformFromFile( f, Dokument.class);
            System.out.println( "ilosc wystapien:" + d.getWystapienia().size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return d;
    }

    private void test(){
        initDB();

        Connection conn = getConnection();


        //creat table



        Statement stmt;
        ResultSet resultSet;



        //Display URL and connection information
        System.out.println("Connection: " + conn);

        //Get a Statement object
        try {

            stmt = conn.createStatement();

            //create table
            int result =  stmt.executeUpdate( SQLS.CREATE_SQL);
            Logger.getAnonymousLogger().info( "created table:" + result);



            //iterate and displan through resultsset



        } catch (SQLException e) {
            e.printStackTrace();
        }

        //execute query



    }



}
