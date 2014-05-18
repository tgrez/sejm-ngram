package org.sejmngram.database.fetcher.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.sejmngram.database.fetcher.converter.IdConverter;
import org.sejmngram.database.fetcher.converter.NgramFtsConverter;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.model.Record;

// TODO exception handling
public class MySqlFtsDbConnector implements DbConnector {

	private static final String USERNAME = "db-fetcher";
	private static final String PASSWORD = "sejmngram2";
	private static final String URL = "jdbc:mysql://localhost:3306/sejmngram"
			+ "?autoReconnect=true&characterEncoding=utf-8"
			+ "&useUnicode=true";

	private static final String poselIdFilename = "../psc-data/partiaId.json";
	private static final String partiaIdFilename = "../psc-data/poselId.json";

	private Connection conn = null;

	private NgramFtsConverter ngramFtsConverter = null;

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
		ArrayList<Record> result = queryDatabase(ngramName);
		return ngramFtsConverter.dbRecordsToNgramResponse(ngramName, result);
	}

	private ArrayList<Record> queryDatabase(String ngramName) {
		ArrayList<Record> results = new ArrayList<Record>();

		try {
			String query = "SELECT date, SUM(term_count(textNormalized, ?)) AS count " +
					"FROM wystapienia WHERE MATCH (textNormalized) AGAINST ( concat('\"', ?, '\"') IN BOOLEAN MODE) " +
					"GROUP BY date";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, ngramName);
			pstmt.setString(2, ngramName);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				results.add(new Record(resultSet.getDate("date"), resultSet.getInt("count")));
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	private void readIdFiles(String partyFilename, String poselFilename) {
		ngramFtsConverter = new NgramFtsConverter(
				new IdConverter(partyFilename), new IdConverter(poselFilename));
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
