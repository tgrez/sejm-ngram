package org.sejmngram.common.json.datamodel;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.sejmngram.common.json.JsonDateSerializer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "id", "posel", "tytul", "data", "stanowisko", "partia", "tresc" })
public class WystapienieProcessed {

	@JsonProperty("posel")
	private int posel;

	@JsonProperty("tytul")
	private String tytul;

	@JsonProperty("data")
	private Date data;


	@JsonProperty("stanowisko")
	private String stanowisko;


	@JsonProperty("partia")
	private int partia;


	@JsonProperty("tresc")
	private String tresc;

    @JsonProperty("id")
    private String id;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("posel")
	public int getPosel() {
		return posel;
	}

	@JsonProperty("posel")
	public void setPosel(int posel) {
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
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(Date data) {
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
	public int getPartia() {
		return partia;
	}

	@JsonProperty("partia")
	public void setPartia(int partia) {
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

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

    @Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
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