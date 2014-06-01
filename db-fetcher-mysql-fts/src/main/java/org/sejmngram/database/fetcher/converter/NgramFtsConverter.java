package org.sejmngram.database.fetcher.converter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.ResponseBuilder;
import org.sejmngram.database.fetcher.model.Record;

public class NgramFtsConverter {
	
	private IdConverter partyConverter;
	private IdConverter poselConverter;
	
	private static final int SINGLE_BLOB_SIZE = 18;
	
	public NgramFtsConverter(IdConverter partyConverter, IdConverter poselConverter) {
		this.partyConverter = partyConverter;
		this.poselConverter = poselConverter;
	}
	
	public NgramResponse dbRecordsToNgramResponse(String ngramName, List<Record> records) {
		ResponseBuilder responesBuilder = new ResponseBuilder(ngramName);

        for (Record r : records){
            responesBuilder.addOccurances(Integer.toString(r.getPartyId()), r.getDate().toString(), r.getCount());
        }

        return responesBuilder.generateResponse();
	}
	
	public int fromByteArray(byte[] bytes) {
	     return ByteBuffer.wrap(bytes).getInt();
	}
}
