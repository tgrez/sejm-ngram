package org.sejmngram.database.fetcher.json.datamodel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by michalsiemionczyk on 26/07/14.
 */
public class NgramOccurencesResponse {

    private Meta meta;

    List<Occurence> occurences;

    public List<Occurence> getOccurences() {
        return occurences;
    }

    public void setOccurences(List<Occurence> occurences) {
        this.occurences = occurences;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public static NgramOccurencesResponse getFakeOne(){
        NgramOccurencesResponse nReseponse = new NgramOccurencesResponse();
        nReseponse.setMeta( new Meta(
                666, 3, 13, 20
        ));

        Occurence oc1 = new Occurence(new Date(), 13, 14, "text occurence nr 1");
        Occurence oc2 = new Occurence(new Date(), 14, 15, "text occurence nr 2");
        nReseponse.setOccurences(Arrays.asList(new Occurence[] {oc1, oc2}));

        return nReseponse;
    }
}
