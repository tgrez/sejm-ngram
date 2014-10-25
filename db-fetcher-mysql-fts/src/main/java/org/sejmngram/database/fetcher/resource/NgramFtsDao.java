package org.sejmngram.database.fetcher.resource;

import java.util.List;

import org.sejmngram.database.fetcher.model.Record;
import org.sejmngram.database.fetcher.model.RecordMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(RecordMapper.class)
public interface NgramFtsDao {

	@SqlQuery("SELECT date, partyId,  SUM(term_count(textNormalized, :ngram)) AS count " +
			  "FROM wystapienia WHERE MATCH (textNormalized) AGAINST " +
			  "( concat('\"', :ngram, '\"') IN BOOLEAN MODE) GROUP BY date, partyId")
	List<Record> searchFts(@Bind("ngram") String ngram);



	
	void close();
}
