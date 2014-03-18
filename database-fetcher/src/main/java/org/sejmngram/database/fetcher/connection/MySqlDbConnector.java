package org.sejmngram.database.fetcher.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.maven.example.tables.Ngrams;
import org.sejmngram.database.fetcher.converter.IdConverter;
import org.sejmngram.database.fetcher.converter.NgramConverter;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

import com.yammer.metrics.annotation.Timed;

// TODO exception handling
public class MySqlDbConnector implements DbConnector {

	private static final String USERNAME = "db-fetcher";
	private static final String PASSWORD = "sejmngram2";
	private static final String URL = "jdbc:mysql://localhost:3306/sejmngram";

	private Connection conn = null;
	
	private NgramConverter ngramConverter = null;
	
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
	public NgramResponse retrieve(String ngramName, Date from, Date to, int partyId) {
        Result<Record> result = queryDatabase(ngramName, from, to);
		return ngramConverter.dbRecordsToNgramResponse(ngramName, result);
	}

	@Timed
	private Result<Record> queryDatabase(String ngramName, Date from, Date to) {
        DSLContext context = DSL.using(conn, SQLDialect.MYSQL);
        Result<Record> result = context.select()
        		.from(Ngrams.NGRAMS)
        		.where(Ngrams.NGRAMS.NGRAM.equal(ngramName)
//        				.and(Ngrams.NGRAMS.DATEFROM.lessOrEqual(new Timestamp(to.getTime())))
//        				.and(Ngrams.NGRAMS.DATETO.greaterOrEqual(new Timestamp(from.getTime())))
        				)
        		.fetch();
        return result;
	}
	
	@Override
	public void readIdFiles(String partyFilename, String poselFilename) {
		ngramConverter = new NgramConverter(
				new IdConverter(partyFilename),
				new IdConverter(poselFilename));
	}

}
