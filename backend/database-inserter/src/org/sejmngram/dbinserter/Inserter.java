package org.sejmngram.dbinserter;

import java.sql.*;

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
        ins.test();

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

    private void test(){
        initDB();

        Connection conn = getConnection();

        Statement stmt;
        ResultSet resultSet;

        //Display URL and connection information
        System.out.println("Connection: " + conn);

        //Get a Statement object
        try {

            stmt = conn.createStatement();
            resultSet =  stmt.executeQuery(db_select_query);

            //iterate and displan through resultsset
            System.out.println("id, user_id,  value\n");
            while(resultSet.next()){
                System.out.format("%d   %d  %f", resultSet.getInt("id"), resultSet.getInt("user_id"), resultSet.getDouble("value"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        //execute query



    }



}
