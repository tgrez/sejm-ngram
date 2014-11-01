package org.sejmngram.database.fetcher.converter;

import java.util.List;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.ResponseBuilder;
import org.sejmngram.database.fetcher.model.Record;

public class NgramFtsConverter {

    private IdConverter partyConverter;
    private IdConverter poselConverter;

    public NgramFtsConverter(IdConverter partyConverter,
            IdConverter poselConverter) {
        this.partyConverter = partyConverter;
        this.poselConverter = poselConverter;
    }

    public NgramResponse dbRecordsToNgramResponse(String ngramName,
            List<Record> records) {
        ResponseBuilder responesBuilder = new ResponseBuilder(ngramName);

        for (Record r : records) {
            String partyName = partyConverter.resolve(r.getPartyId());
            responesBuilder.addOccurances(partyName, r.getDate().toString(),
                    r.getCount());
        }

        return responesBuilder.generateResponse();
    }
}
