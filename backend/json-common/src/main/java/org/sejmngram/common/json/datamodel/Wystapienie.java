package org.sejmngram.common.json.datamodel;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "posel", "tytul", "data", "stanowisko", "partia", "tresc" })
public class Wystapienie {

	@JsonProperty("posel")
	private String posel;
	@JsonProperty("tytul")
	private String tytul;
	@JsonProperty("data")
	private String data;
	@JsonProperty("stanowisko")
	private String stanowisko;
	@JsonProperty("partia")
	private String partia;
	@JsonProperty("tresc")
	private String tresc;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("posel")
	public String getPosel() {
		return posel;
	}

	@JsonProperty("posel")
	public void setPosel(String posel) {
		this.posel = posel;
	}

	@JsonProperty("tytul")
	public String getTytul() {
		return tytul;
	}

	@JsonProperty("tytul")
	public void setTytul(String tytul) {
		this.tytul = tytul;
	}

	@JsonProperty("data")
	public String getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(String data) {
		this.data = data;
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

	@JsonProperty("tresc")
	public String getTresc() {
		return tresc;
	}

	@JsonProperty("tresc")
	public void setTresc(String tresc) {
		this.tresc = tresc;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}