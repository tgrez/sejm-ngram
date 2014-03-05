package org.sejmngram.dbinserter;

import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Dokument;
import org.sejmngram.common.json.datamodel.Wystapienie;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by michalsiemionczyk on 14/02/14.
 */
public class Inserter {




    public static String db_host = "localhost";
    static String db_base = "sejmngram";
    static String db_user = "root";
    static String db_pass = "";
    static String db_select_query = "select * from ngrams";



    public static void main (String[] args){

        Inserter ins = new Inserter();


        //read dokuemnt
//        "./scripts/sejmometr/downloadedData/2011-11-19.json"
        ArrayList<Dokument> d = ins.getDokumentFromJsonFile("./scripts/sejmometr/downloadedData/");

        //get map of blobs
        HashMap<String, RowData> blobMap = ins.getMapOfBlobs(d);

        performAnalysis( blobMap );

        ins.insertToDb(blobMap);



        System.out.println( " Hello world!");
    }

    private void insertToDb( HashMap<String, RowData> blobMap) {
        initDB();
         Connection c = getConnection();



        PreparedStatement stmt;
        ResultSet resultSet;



        //Display URL and connection information
        System.out.println("Connection: " + c);

        //Get a Statement object
        try {

       /*
     CREATE TABLE ngrams (
            id INT NOT NULL AUTO_INCREMENT,
            datefrom DATETIME,
            dateto DATETIME,
            ngram VARCHAR(20),
            nrOccurences INT,
            content TEXT,
            PRIMARY KEY (id)

        );
            */

            int nrInsertedRows = 0;
            for ( String ngram :  blobMap.keySet()){
                RowData rowData = blobMap.get( ngram );
                for ( RowData.Row row :  rowData.getAllRows()){
                    stmt = c.prepareStatement("INSERT INTO sejmngram.ngrams values ( default, ?, ?, ?, ?, ?)");

                    stmt.setDate( 1, new java.sql.Date( rowData.getDateFrom().getTime() ));
                    stmt.setDate( 2, new java.sql.Date( rowData.getDateTo().getTime() ));
                    stmt.setString( 3, ngram );
                    stmt.setInt( 4, row.getNrEntries());
                    stmt.setString( 5, row.getBlob() );

                    //create table
                    int result =  stmt.executeUpdate();
                    Logger.getAnonymousLogger().info( "inserted row :" + result);
                    nrInsertedRows++;
                }
            }


            Logger.getAnonymousLogger().info( "nrAllInsertedRows: " + nrInsertedRows);


            //String key = "na";
            //RowData row = blobMap.get( key );



            //iterate and displan through resultsset
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void segmentBlob(HashMap<String, RowData> blobMap) {

    }

    private static void performAnalysis(HashMap<String,RowData> blobsMap) {


        System.out.println( "\nnr different words:" + blobsMap.keySet().size());
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        String keyMin = "";
        String keyMax = "";
        ArrayList<Integer> entriesSizes = new ArrayList<Integer>();
        long blobBytes = 0;

        for ( String key :  blobsMap.keySet() ){
            RowData d = blobsMap.get( key );

            blobBytes += getStringSizeInBytes( d.getLastBlob() );

            int keySize = blobsMap.get( key ).getNrAllEntries();
            entriesSizes.add( keySize );
            if ( keySize > max ){
                max = keySize;
                keyMax = key;
            }
            if ( keySize < min ){
                min = keySize;
                keyMin = key;
            }
        }


        System.out.println( "\nblobs in [KB]: "+ blobBytes / 1024 + " in [MB]: " + blobBytes/(1024*1024));

        System.out.println( "\nmin nrOccurences" + min + " for : " + keyMin);
        System.out.println( "\nmax  nrOccurences" + max + " for: "+ keyMax );
        System.out.println("\nblob for max:" + blobsMap.get( keyMax ).getLastBlob());


        System.out.println( "\n-- 10 top blob enrties nr: --");
        Comparator<Integer> comparator = Collections.reverseOrder();
        Collections.sort( entriesSizes, comparator );
        for ( int i = 0; i < 10; i++){
            System.out.println( "blob entries:" + entriesSizes.get( i));
        }





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

    private HashMap<String, RowData> getMapOfBlobs(ArrayList<Dokument> dokuments){

//        unixtimestamp(seconds)%Cezary Grarczyk%PlatformaObywatelska^unixtimestamp%AleksanderOlechowski%PlatformaObywatelska^

        HashMap<String, RowData> blobsMap = new HashMap<String, RowData>();


        int i = 0 ;
        int nrAllDokuments = dokuments.size();

        for ( Dokument d : dokuments ){
            System.out.println("processing dokument " + i + " of " + nrAllDokuments);
            for ( Wystapienie wyst : d.getWystapienia()){
                String[] words = wyst.getTresc().split(" ");
                long unixPosixTimestamp = wyst.getData().getTime() / 1000;
                String posel = wyst.getPosel();
                String partia = wyst.getPartia();

                for ( String word : words ){

                    if ( skipWord( word )) continue;;   //apply skip rules

                    RowData rowData = null;

                    //check it word exists
                    if ( blobsMap.containsKey( word )){
                        rowData = blobsMap.get( word );
                    } else {
                        rowData = new RowData();

                        //sets dateFrom
                        rowData.setDateFrom( wyst.getData() );
                        rowData.setDateTo( wyst.getData());
                    }

                    //rowData.inreaseNrEntries();
                    rowData.addEntryToBlob( unixPosixTimestamp, posel, partia );

                    //save blob to hashmap
                    blobsMap.put( word, rowData );
                }
            }
            i++;

        }

        return blobsMap;





    }

    private ArrayList<Dokument> getDokumentFromJsonFile( String path ){

//        File fA = new File("./scripts");
//        for (File fL : fA.listFiles()){
//            Logger.getAnonymousLogger().info( fL.getPath());
//        }

        //for maven build use ".."
        File f = new File( path);

        File[] files = f.listFiles();

        ArrayList<Dokument> dokuments = new ArrayList<Dokument>();

        try {
            for ( File jsonFile : files){
                Dokument d = JsonProcessor.transformFromFile( jsonFile, Dokument.class);
                dokuments.add( d );
                System.out.println("ilosc wytapien w pliku json:" + d.getWystapienia().size());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dokuments;
    }

    /** Applies rules of skipping word*/
    private boolean skipWord( String word){
        //cehck if not length of 1
        if ( word.length() == 1) return true;

        return false;
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

    public static int getStringSizeInBytes( String s ){
//        Minimum String memory usage (bytes) = 8 * (int) ((((no chars) * 2) + 45) / 8)
        return 8 * (int) (((( s.length()) * 2) + 45) / 8);

    }



}
