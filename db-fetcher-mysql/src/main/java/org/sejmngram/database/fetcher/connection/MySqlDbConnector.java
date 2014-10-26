package org.sejmngram.database.fetcher.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.maven.example.tables.Ngrams;
import org.sejmngram.database.fetcher.converter.IdConverter;
import org.sejmngram.database.fetcher.converter.NgramConverter;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;

public class MySqlDbConnector implements DbConnector {

	private static final String USERNAME = "db-fetcher";
	private static final String PASSWORD = "sejmngram2";
	private static final String URL = "jdbc:mysql://localhost:3306/sejmngram" +
										 "?autoReconnect=true&characterEncoding=utf-8" +
										 "&useUnicode=true";
	
	private static final String poselIdFilename = "../psc-data/partiaId.json";
	private static final String partiaIdFilename = "../psc-data/poselId.json";

	private Connection conn = null;
	
	private NgramConverter ngramConverter = null;
	
	@Override
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			readIdFiles(partiaIdFilename, poselIdFilename);
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
	public NgramResponse retrieve(String ngramName) {
        Result<Record> result = queryDatabase(ngramName);
		return ngramConverter.dbRecordsToNgramResponse(ngramName, result);
	}

	private Result<Record> queryDatabase(String ngramName) {
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
	
	private void readIdFiles(String partyFilename, String poselFilename) {
		ngramConverter = new NgramConverter(
				new IdConverter(partyFilename),
				new IdConverter(poselFilename));
	}

	@Override
	public NgramResponse retrieveByParty(String ngram, int partyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NgramResponse retrieveByPosel(String ngram, int poselId) {
		// TODO Auto-generated method stub
		return null;
	}

}
