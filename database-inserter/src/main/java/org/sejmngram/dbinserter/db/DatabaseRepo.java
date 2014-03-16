package org.sejmngram.dbinserter.db;

import org.codehaus.jackson.type.TypeReference;
import org.sejmngram.common.json.JsonProcessor;
import org.sejmngram.common.json.datamodel.Wystapienie;
import org.sejmngram.common.json.datamodel.WystapienieProcessed;
import org.sejmngram.dbinserter.model.RowData;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by michalsiemionczyk on 05/03/14.
 */
public class DatabaseRepo {

    private   String db_host = "localhost";
    private  String db_base = "sejmngram";
    private  String db_user = "root";
    private  String db_pass = "";
    private  String db_table_name = "";

    Connection c;

    public DatabaseRepo( String tableName){
        initDB();
        this.db_table_name = tableName;
        this.c = getConnection();

    }

    public void insertJsonFileToDb( File jsonWystapienieFile ){

        ArrayList<WystapienieProcessed> wystapienia = null;
        try {
            wystapienia = JsonProcessor.transformFromFile( jsonWystapienieFile, new TypeReference<ArrayList<WystapienieProcessed>>() {});

        } catch (IOException e) {
            e.printStackTrace();
        }

        Connection c = getConnection();
        PreparedStatement stmt;

        try {
            int nrInsertedRows = 0;

            for ( WystapienieProcessed wyst : wystapienia){
                stmt = c.prepareStatement("INSERT INTO " + this.db_table_name + " values ( default, ?, ?, ?, ?, ?, ?)");





                stmt.setDate( 1, new java.sql.Date(wyst.getData().getTime()));
                stmt.setString(2, wyst.getId());
                stmt.setInt(3, wyst.getPartia());
                stmt.setInt(4, wyst.getPosel());

                String rawTresc = processTresc(wyst.getTresc());
                if ( skipWord( rawTresc)) continue;
                stmt.setString(5, processTresc(wyst.getTresc()));
                stmt.setString(6, wyst.getTytul());

                int result =  stmt.executeUpdate();
                nrInsertedRows++;
                if ( nrInsertedRows % 500 == 0){
                    Logger.getAnonymousLogger().info( "inserted rows :" + nrInsertedRows);
                }

//
//                CREATE TABLE ngramsTS (
//                        id INT NOT NULL AUTO_INCREMENT,
//                        data DATETIME,
//                        wystapienieID INT,
//                        partiaID INT,
//                        poselID INT,
//                        tresc TEXT,
//                        tytul TEXT,
//                        PRIMARY KEY (id)
//                );
                 


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** removes uncessecary things*/
    public String processTresc( String rawTresc){
        String words = rawTresc.replaceAll("[^\\p{L}\\p{Nd}]", " ").toLowerCase();
        words = Normalizer.normalize(words, Normalizer.Form.NFD);
//        words = words.replaceAll("[^\\p{ASCII}]", "");
//        words = words.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        words  = words.replaceAll("\\p{M}", "").replaceAll("ł", "l");

        return words;

    }

    private static boolean skipWord( String word){
        //cehck if not length of 1
        if (word.length() == 1 || word.length() == 0) return true;
        return false;
    }



    public  void insertToDb( HashMap<String, RowData> blobMap) {

        initDB();
        Connection c = getConnection();
        PreparedStatement stmt;
        System.out.println("Connection: " + c);

        try {
            int nrInsertedRows = 0;
            for ( String ngram :  blobMap.keySet()){
                RowData rowData = blobMap.get( ngram );
                for ( RowData.Row row :  rowData.getAllRows()){
                    stmt = c.prepareStatement("INSERT INTO " + this.db_table_name + " values ( default, ?, ?, ?, ?, ?)");

                    stmt.setDate(1, new java.sql.Date(row.getDateFrom().getTime()));
                    stmt.setDate(2, new java.sql.Date(row.getDateTo().getTime()));
                    stmt.setString(3, ngram);
                    stmt.setInt(4, row.getNrEntries());
                    stmt.setBytes(5, row.getBlob());

                    int result =  stmt.executeUpdate();
                    nrInsertedRows++;
                    if ( nrInsertedRows % 500 == 0){
                        Logger.getAnonymousLogger().info( "inserted rows :" + nrInsertedRows);
                    }

                }

            }

            Logger.getAnonymousLogger().info( "nrAllInsertedRows: " + nrInsertedRows);
        } catch (SQLException e) {
            e.printStackTrace();
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
}
