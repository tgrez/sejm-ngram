package org.sejmngram.database.fetcher.converter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.maven.example.tables.Ngrams;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.ResponseBuilder;

public class NgramConverter {
	
	private IdConverter partyConverter;
	private IdConverter poselConverter;
	
	private static final int SINGLE_BLOB_SIZE = 18;
	
	public NgramConverter(IdConverter partyConverter, IdConverter poselConverter) {
		this.partyConverter = partyConverter;
		this.poselConverter = poselConverter;
	}
	
	public NgramResponse dbRecordsToNgramResponse(String ngramName, List<Record> records) {
		ResponseBuilder responesBuilder = new ResponseBuilder(ngramName);
		byte[] currentDateByte = new byte[10];
		byte[] poselIdByte = new byte[4];
		byte[] partiaIdByte = new byte[4];
        for (Record r : records) {
        	int count = r.getValue(Ngrams.NGRAMS.NROCCURENCES).intValue();
            byte[] blob = r.getValue(Ngrams.NGRAMS.CONTENT);
            for (int i = 0; i < count * SINGLE_BLOB_SIZE; i += SINGLE_BLOB_SIZE) {
            	System.arraycopy(blob, i, currentDateByte, 0, 10);
            	System.arraycopy(blob, i + 10, poselIdByte, 0, 4);
            	System.arraycopy(blob, i + 14, partiaIdByte, 0, 4);
            	int partyId = fromByteArray(partiaIdByte);
            	responesBuilder.addOccurance(
            			partyConverter.resolve(partyId),
            			new String(currentDateByte, StandardCharsets.UTF_8));
            }
        }
		return responesBuilder.generateResponse();
	}
	
	public int fromByteArray(byte[] bytes) {
	     return ByteBuffer.wrap(bytes).getInt();
	}
}
