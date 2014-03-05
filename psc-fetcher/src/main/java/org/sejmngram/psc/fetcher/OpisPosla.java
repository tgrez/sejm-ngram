package org.sejmngram.psc.fetcher;

import org.codehaus.jackson.annotate.JsonProperty;

public class OpisPosla {

    @JsonProperty("posel")
    private String posel;
    @JsonProperty("stanowisko")
    private String stanowisko;
    @JsonProperty("partia")
    private String partia;
    @JsonProperty("objecie_stanowiska")
    private String objecie_stanowiska;

    @JsonProperty("posel")
    public String getPosel() {
        return posel;
    }

    @JsonProperty("posel")
    public void setPosel(String posel) {
        this.posel = posel;
    }

    @JsonProperty("stanowisko")
    public String getStanowisko() {
        return stanowisko;
    }

    @JsonProperty("stanowisko")
    public void setStanowisko(String stanowisko) {
        this.stanowisko = stanowisko;
    }

    @JsonProperty("partia")
    public String getPartia() {
        return partia;
    }

    @JsonProperty("partia")
    public void setPartia(String partia) {
        this.partia = partia;
    }

    @JsonProperty("objecie_stanowiska")
    public String getObjecie_stanowiska() {
        return objecie_stanowiska;
    }

    @JsonProperty("objecie_stanowiska")
    public void setObjecie_stanowiska(String objecie_stanowiska) {
        this.objecie_stanowiska = objecie_stanowiska;
    }


}
