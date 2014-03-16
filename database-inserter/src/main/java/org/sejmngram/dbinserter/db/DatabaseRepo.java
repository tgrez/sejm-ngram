package org.sejmngram.dbinserter.db;

import org.sejmngram.dbinserter.model.RowData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.logging.Logger;

/**
 * Created by michalsiemionczyk on 05/03/14.
 */
public class DatabaseRepo {

    private  static String db_host = "localhost";
    private static String db_base = "sejmngram";
    private static String db_user = "root";
    private static String db_pass = "";

    Connection c;

    public DatabaseRepo(){
        initDB();
        this.c = getConnection();

    }

    public  void insertToDb( HashMap<String, RowData> blobMap) {

        initDB();
        Connection c = getConnection();
        PreparedStatement stmt;
        System.out.println("Connection: " + c);

        try {
            int nrInsertedRows = 0;
            for (String ngram :  blobMap.keySet()) {
                RowData rowData = blobMap.get( ngram );
                Iterator<RowData.Row> rowIterator = rowData.getAllRows().iterator();
                for (;rowIterator.hasNext();) {
                    stmt = c.prepareStatement("INSERT INTO sejmngram.ngrams values ( default, ?, ?, ?, ?, ?)");

                    RowData.Row row = rowIterator.next();

                    stmt.setDate(1, new java.sql.Date(row.getDateFrom().getTime()));
                    stmt.setDate(2, new java.sql.Date(row.getDateTo().getTime()));
                    stmt.setString(3, ngram);
                    stmt.setInt(4, row.getNrEntries());
                    stmt.setBytes(5, row.getBlob());

                    int result =  stmt.executeUpdate();
                    nrInsertedRows++;
                    if ( nrInsertedRows % 500 == 0) {
                        Logger.getAnonymousLogger().info( "inserted rows :" + nrInsertedRows);
                    }

                    rowIterator.remove();
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
