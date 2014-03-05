package org.sejmngram.sejmometr.json.datamodel.klub;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "id", "liczba_kobiet", "liczba_mezczyzn", "liczba_poslow",
		"nazwa", "skrot", "srednia_frekfencja",
		"srednia_liczba_projektow_uchwal", "srednia_liczba_projektow_ustaw",
		"srednia_liczba_wnioskow", "srednia_liczba_wystapien",
		"srednia_poparcie_w_okregu", "srednia_udzial_w_obradach",
		"srednia_zbuntowanie" })
public class KlubData {

	@JsonProperty("id")
	private String id;
	@JsonProperty("liczba_kobiet")
	private Integer liczba_kobiet;
	@JsonProperty("liczba_mezczyzn")
	private Integer liczba_mezczyzn;
	@JsonProperty("liczba_poslow")
	private Integer liczba_poslow;
	@JsonProperty("nazwa")
	private String nazwa;
	@JsonProperty("skrot")
	private String skrot;
	@JsonProperty("srednia_frekfencja")
	private String srednia_frekfencja;
	@JsonProperty("srednia_liczba_projektow_uchwal")
	private String srednia_liczba_projektow_uchwal;
	@JsonProperty("srednia_liczba_projektow_ustaw")
	private String srednia_liczba_projektow_ustaw;
	@JsonProperty("srednia_liczba_wnioskow")
	private String srednia_liczba_wnioskow;
	@JsonProperty("srednia_liczba_wystapien")
	private String srednia_liczba_wystapien;
	@JsonProperty("srednia_poparcie_w_okregu")
	private String srednia_poparcie_w_okregu;
	@JsonProperty("srednia_udzial_w_obradach")
	private String srednia_udzial_w_obradach;
	@JsonProperty("srednia_zbuntowanie")
	private String srednia_zbuntowanie;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("liczba_kobiet")
	public Integer getLiczba_kobiet() {
		return liczba_kobiet;
	}

	@JsonProperty("liczba_kobiet")
	public void setLiczba_kobiet(Integer liczba_kobiet) {
		this.liczba_kobiet = liczba_kobiet;
	}

	@JsonProperty("liczba_mezczyzn")
	public Integer getLiczba_mezczyzn() {
		return liczba_mezczyzn;
	}

	@JsonProperty("liczba_mezczyzn")
	public void setLiczba_mezczyzn(Integer liczba_mezczyzn) {
		this.liczba_mezczyzn = liczba_mezczyzn;
	}

	@JsonProperty("liczba_poslow")
	public Integer getLiczba_poslow() {
		return liczba_poslow;
	}

	@JsonProperty("liczba_poslow")
	public void setLiczba_poslow(Integer liczba_poslow) {
		this.liczba_poslow = liczba_poslow;
	}

	@JsonProperty("nazwa")
	public String getNazwa() {
		return nazwa;
	}

	@JsonProperty("nazwa")
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	@JsonProperty("skrot")
	public String getSkrot() {
		return skrot;
	}

	@JsonProperty("skrot")
	public void setSkrot(String skrot) {
		this.skrot = skrot;
	}

	@JsonProperty("srednia_frekfencja")
	public String getSrednia_frekfencja() {
		return srednia_frekfencja;
	}

	@JsonProperty("srednia_frekfencja")
	public void setSrednia_frekfencja(String srednia_frekfencja) {
		this.srednia_frekfencja = srednia_frekfencja;
	}

	@JsonProperty("srednia_liczba_projektow_uchwal")
	public String getSrednia_liczba_projektow_uchwal() {
		return srednia_liczba_projektow_uchwal;
	}

	@JsonProperty("srednia_liczba_projektow_uchwal")
	public void setSrednia_liczba_projektow_uchwal(
			String srednia_liczba_projektow_uchwal) {
		this.srednia_liczba_projektow_uchwal = srednia_liczba_projektow_uchwal;
	}

	@JsonProperty("srednia_liczba_projektow_ustaw")
	public String getSrednia_liczba_projektow_ustaw() {
		return srednia_liczba_projektow_ustaw;
	}

	@JsonProperty("srednia_liczba_projektow_ustaw")
	public void setSrednia_liczba_projektow_ustaw(
			String srednia_liczba_projektow_ustaw) {
		this.srednia_liczba_projektow_ustaw = srednia_liczba_projektow_ustaw;
	}

	@JsonProperty("srednia_liczba_wnioskow")
	public String getSrednia_liczba_wnioskow() {
		return srednia_liczba_wnioskow;
	}

	@JsonProperty("srednia_liczba_wnioskow")
	public void setSrednia_liczba_wnioskow(String srednia_liczba_wnioskow) {
		this.srednia_liczba_wnioskow = srednia_liczba_wnioskow;
	}

	@JsonProperty("srednia_liczba_wystapien")
	public String getSrednia_liczba_wystapien() {
		return srednia_liczba_wystapien;
	}

	@JsonProperty("srednia_liczba_wystapien")
	public void setSrednia_liczba_wystapien(String srednia_liczba_wystapien) {
		this.srednia_liczba_wystapien = srednia_liczba_wystapien;
	}

	@JsonProperty("srednia_poparcie_w_okregu")
	public String getSrednia_poparcie_w_okregu() {
		return srednia_poparcie_w_okregu;
	}

	@JsonProperty("srednia_poparcie_w_okregu")
	public void setSrednia_poparcie_w_okregu(String srednia_poparcie_w_okregu) {
		this.srednia_poparcie_w_okregu = srednia_poparcie_w_okregu;
	}

	@JsonProperty("srednia_udzial_w_obradach")
	public String getSrednia_udzial_w_obradach() {
		return srednia_udzial_w_obradach;
	}

	@JsonProperty("srednia_udzial_w_obradach")
	public void setSrednia_udzial_w_obradach(String srednia_udzial_w_obradach) {
		this.srednia_udzial_w_obradach = srednia_udzial_w_obradach;
	}

	@JsonProperty("srednia_zbuntowanie")
	public String getSrednia_zbuntowanie() {
		return srednia_zbuntowanie;
	}

	@JsonProperty("srednia_zbuntowanie")
	public void setSrednia_zbuntowanie(String srednia_zbuntowanie) {
		this.srednia_zbuntowanie = srednia_zbuntowanie;
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