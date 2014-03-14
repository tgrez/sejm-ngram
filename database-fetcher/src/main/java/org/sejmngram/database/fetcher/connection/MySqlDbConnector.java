package org.sejmngram.database.fetcher.connection;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.maven.example.tables.Ngrams;
import org.sejmngram.database.fetcher.converter.Converter;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

// TODO exception handling
public class MySqlDbConnector implements DbConnector {

	private static final String USERNAME = "db-fetcher";
	private static final String PASSWORD = "sejmngram2";
	private static final String URL = "jdbc:mysql://localhost:3306/sejmngram";

	private Connection conn = null;
	
	@Override
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	@Override
	public void disconnect() {
		if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }
	}

	@Override
	public NgramResponse retrieve(String ngramName, Date from, Date to, int partyId)
			throws UnsupportedEncodingException {
        DSLContext context = DSL.using(conn, SQLDialect.MYSQL);
        Result<Record> result = context.select()
        		.from(Ngrams.NGRAMS)
        		.where(Ngrams.NGRAMS.NGRAM.equal(ngramName)
//        				.and(Ngrams.NGRAMS.DATEFROM.lessOrEqual(new Timestamp(to.getTime())))
//        				.and(Ngrams.NGRAMS.DATETO.greaterOrEqual(new Timestamp(from.getTime())))
        				)
        		.fetch();
        // TODO use party id
		return Converter.dbRecordsToNgramResponse(ngramName, result);
	}

}
