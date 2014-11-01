package org.sejmngram.database.fetcher.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class RecordMapper implements ResultSetMapper<Record> {
    public Record map(int index, ResultSet resultSet, StatementContext ctx)
            throws SQLException {
        return new Record(resultSet.getDate("date"), resultSet.getInt("count"),
                resultSet.getInt("partyId"));
    }
}