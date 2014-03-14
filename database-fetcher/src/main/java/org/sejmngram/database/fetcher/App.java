package org.sejmngram.database.fetcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.maven.example.tables.Ngrams;
import org.jooq.util.maven.example.tables.records.NgramsRecord;

public class App {
	
	private static final Logger LOG = Logger.getLogger(App.class);

	// TODO move database creation to maven?
	public static void main(String[] args) throws IOException {
		Connection conn = null;

        String userName = "db-fetcher";
        String password = "sejmngram2";
        String url = "jdbc:mysql://localhost:3306/sejmngram";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(Ngrams.NGRAMS).fetch();
            for (Record r : result) {
                Integer id = r.getValue(Ngrams.NGRAMS.ID);
                Timestamp firstName = r.getValue(Ngrams.NGRAMS.DATEFROM);
                Timestamp lastName = r.getValue(Ngrams.NGRAMS.DATETO);
                String ngram = r.getValue(Ngrams.NGRAMS.NGRAM);
                String blob = r.getValue(Ngrams.NGRAMS.CONTENT);

                System.out.println("ID: " + id + " ngram: " + ngram + " date from: " + firstName + " dateto: " + lastName + " blob: " + blob);
                return;
            }
        } catch (Exception e) {
            // For the sake of this tutorial, let's keep exception handling simple
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }
        }
		
//		if (args.length == 0) {
//			System.out.println("Use one of following command line parameters:");
//			System.out.println("\tgenerate");
//			System.out.println("\tread");
//			return;
//		}
//		System.out.println("Using option: " + args[0]);
//		if ("generate".equals(args[0])) {
//			if (args.length > 1 && args[1] != null) {
//				MockFileGenerator.generateMockFile(Integer.valueOf(args[1]));
//			} else {
//				MockFileGenerator.generateMockFile(30);
//			}
//		} else if ("read".equals(args[0])) {
//			testBlobReading();
//		}
	}

	private static void testBlobReading() throws UnsupportedEncodingException {
		int partyId = 10;
		DbManager db = new DbManager();
		long startTime = System.nanoTime();
		LOG.debug("Start time:\t" + startTime);
		db.filterByParty("whatever", partyId);
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		LOG.debug("End time:\t" + endTime);
		LOG.debug("Total time:\t" + duration);
		LOG.debug("Total time (s):\t" + TimeUnit.NANOSECONDS.toSeconds(duration));
	}

}
