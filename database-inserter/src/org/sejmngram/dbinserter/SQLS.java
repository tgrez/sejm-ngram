package org.sejmngram.dbinserter;

/**
 * Created by michalsiemionczyk on 27/02/14.
 */
public class SQLS {


    public static String CREATE_SQL = "CREATE TABLE IF NOT EXISTS ngrams (    "
            + "id INT,"
            + "datefrom DATETIME,"
            + "dateto DATETIME,"
            + "ngram VARCHAR(10),"
            + "blob BLOB,) ";
}
