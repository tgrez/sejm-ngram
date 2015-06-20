package org.sejmngram.database.fetcher.converter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.ResponseBuilder;
import org.sejmngram.database.fetcher.model.Record;

public class NgramFtsConverter {

    private IdConverter partyConverter;
    private IdConverter poselConverter;
    private final Set<String> dates;

    public NgramFtsConverter(IdConverter partyConverter,
            IdConverter poselConverter, Set<String> dates) {
        this.partyConverter = partyConverter;
        this.poselConverter = poselConverter;
        this.dates = Collections.unmodifiableSet(dates);
    }

    public NgramResponse dbRecordsToNgramResponse(String ngramName,
            List<Record> records) {
        ResponseBuilder responesBuilder = new ResponseBuilder(ngramName, dates);

        for (Record r : records) {
            String partyName = partyConverter.resolve(r.getPartyId());
            responesBuilder.addOccurances(partyName, r.getDate().toString(),
                    r.getCount());
        }

        return responesBuilder.generateResponse();
    }
}
