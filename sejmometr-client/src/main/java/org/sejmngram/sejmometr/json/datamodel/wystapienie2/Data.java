package org.sejmngram.sejmometr.json.datamodel.wystapienie2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.sejmngram.sejmometr.json.SejmometrJsonDateDeserializer;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "ludzie.avatar", "ludzie.id", "ludzie.nazwa",
		"ludzie.posel_id", "sejm_debaty.id", "sejm_debaty.liczba_glosowan",
		"sejm_debaty.liczba_wystapien", "sejm_debaty.typ_id",
		"sejm_debaty.tytul", "data", "id", "posiedzenie_id",
		"czas_rozpoczecia", "czas_zakonczenia", "czlowiek_id", "debata_id",
		"dlugosc", "dzien_sejmowy_id", "ilosc_slow", "klub_id", "kolejnosc",
		"punkt_id", "skrot", "stanowisko_id", "video", "yt_id", "yt_pl_id",
		"stanowiska.id", "stanowiska.nazwa", "tytul", "stanowisko_mowca" })
public class Data {

	@JsonProperty("ludzie.avatar")
	private String ludzie_avatar;
	@JsonProperty("ludzie.id")
	private String ludzie_id;
	@JsonProperty("ludzie.nazwa")
	private String ludzie_nazwa;
	@JsonProperty("ludzie.posel_id")
	private String ludzie_posel_id;
	@JsonProperty("sejm_debaty.id")
	private String sejm_debaty_id;
	@JsonProperty("sejm_debaty.liczba_glosowan")
	private String sejm_debaty_liczba_glosowan;
	@JsonProperty("sejm_debaty.liczba_wystapien")
	private String sejm_debaty_liczba_wystapien;
	@JsonProperty("sejm_debaty.typ_id")
	private String sejm_debaty_typ_id;
	@JsonProperty("sejm_debaty.tytul")
	private String sejm_debaty_tytul;
	@JsonProperty("data")
	private Date data;
	@JsonProperty("id")
	private String id;
	@JsonProperty("posiedzenie_id")
	private String posiedzenie_id;
	@JsonProperty("czas_rozpoczecia")
	private String czas_rozpoczecia;
	@JsonProperty("czas_zakonczenia")
	private String czas_zakonczenia;
	@JsonProperty("czlowiek_id")
	private String czlowiek_id;
	@JsonProperty("debata_id")
	private String debata_id;
	@JsonProperty("dlugosc")
	private String dlugosc;
	@JsonProperty("dzien_sejmowy_id")
	private String dzien_sejmowy_id;
	@JsonProperty("ilosc_slow")
	private String ilosc_slow;
	@JsonProperty("klub_id")
	private String klub_id;
	@JsonProperty("kolejnosc")
	private Long kolejnosc;
	@JsonProperty("punkt_id")
	private String punkt_id;
	@JsonProperty("skrot")
	private String skrot;
	@JsonProperty("stanowisko_id")
	private String stanowisko_id;
	@JsonProperty("video")
	private String video;
	@JsonProperty("yt_id")
	private String yt_id;
	@JsonProperty("yt_pl_id")
	private String yt_pl_id;
	@JsonProperty("stanowiska.id")
	private String stanowiska_id;
	@JsonProperty("stanowiska.nazwa")
	private String stanowiska_nazwa;
	@JsonProperty("tytul")
	private String tytul;
	@JsonProperty("stanowisko_mowca")
	private String stanowisko_mowca;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("ludzie.avatar")
	public String getLudzie_avatar() {
		return ludzie_avatar;
	}

	@JsonProperty("ludzie.avatar")
	public void setLudzie_avatar(String ludzie_avatar) {
		this.ludzie_avatar = ludzie_avatar;
	}

	@JsonProperty("ludzie.id")
	public String getLudzie_id() {
		return ludzie_id;
	}

	@JsonProperty("ludzie.id")
	public void setLudzie_id(String ludzie_id) {
		this.ludzie_id = ludzie_id;
	}

	@JsonProperty("ludzie.nazwa")
	public String getLudzie_nazwa() {
		return ludzie_nazwa;
	}

	@JsonProperty("ludzie.nazwa")
	public void setLudzie_nazwa(String ludzie_nazwa) {
		this.ludzie_nazwa = ludzie_nazwa;
	}

	@JsonProperty("ludzie.posel_id")
	public String getLudzie_posel_id() {
		return ludzie_posel_id;
	}

	@JsonProperty("ludzie.posel_id")
	public void setLudzie_posel_id(String ludzie_posel_id) {
		this.ludzie_posel_id = ludzie_posel_id;
	}

	@JsonProperty("sejm_debaty.id")
	public String getSejm_debaty_id() {
		return sejm_debaty_id;
	}

	@JsonProperty("sejm_debaty.id")
	public void setSejm_debaty_id(String sejm_debaty_id) {
		this.sejm_debaty_id = sejm_debaty_id;
	}

	@JsonProperty("sejm_debaty.liczba_glosowan")
	public String getSejm_debaty_liczba_glosowan() {
		return sejm_debaty_liczba_glosowan;
	}

	@JsonProperty("sejm_debaty.liczba_glosowan")
	public void setSejm_debaty_liczba_glosowan(
			String sejm_debaty_liczba_glosowan) {
		this.sejm_debaty_liczba_glosowan = sejm_debaty_liczba_glosowan;
	}

	@JsonProperty("sejm_debaty.liczba_wystapien")
	public String getSejm_debaty_liczba_wystapien() {
		return sejm_debaty_liczba_wystapien;
	}

	@JsonProperty("sejm_debaty.liczba_wystapien")
	public void setSejm_debaty_liczba_wystapien(
			String sejm_debaty_liczba_wystapien) {
		this.sejm_debaty_liczba_wystapien = sejm_debaty_liczba_wystapien;
	}

	@JsonProperty("sejm_debaty.typ_id")
	public String getSejm_debaty_typ_id() {
		return sejm_debaty_typ_id;
	}

	@JsonProperty("sejm_debaty.typ_id")
	public void setSejm_debaty_typ_id(String sejm_debaty_typ_id) {
		this.sejm_debaty_typ_id = sejm_debaty_typ_id;
	}

	@JsonProperty("sejm_debaty.tytul")
	public String getSejm_debaty_tytul() {
		return sejm_debaty_tytul;
	}

	@JsonProperty("sejm_debaty.tytul")
	public void setSejm_debaty_tytul(String sejm_debaty_tytul) {
		this.sejm_debaty_tytul = sejm_debaty_tytul;
	}

	@JsonProperty("data")
	public Date getData() {
		return data;
	}

	@JsonProperty("data")
	@JsonDeserialize(using = SejmometrJsonDateDeserializer.class)
	public void setData(Date data) {
		this.data = data;
	}

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("posiedzenie_id")
	public String getPosiedzenie_id() {
		return posiedzenie_id;
	}

	@JsonProperty("posiedzenie_id")
	public void setPosiedzenie_id(String posiedzenie_id) {
		this.posiedzenie_id = posiedzenie_id;
	}

	@JsonProperty("czas_rozpoczecia")
	public String getCzas_rozpoczecia() {
		return czas_rozpoczecia;
	}

	@JsonProperty("czas_rozpoczecia")
	public void setCzas_rozpoczecia(String czas_rozpoczecia) {
		this.czas_rozpoczecia = czas_rozpoczecia;
	}

	@JsonProperty("czas_zakonczenia")
	public String getCzas_zakonczenia() {
		return czas_zakonczenia;
	}

	@JsonProperty("czas_zakonczenia")
	public void setCzas_zakonczenia(String czas_zakonczenia) {
		this.czas_zakonczenia = czas_zakonczenia;
	}

	@JsonProperty("czlowiek_id")
	public String getCzlowiek_id() {
		return czlowiek_id;
	}

	@JsonProperty("czlowiek_id")
	public void setCzlowiek_id(String czlowiek_id) {
		this.czlowiek_id = czlowiek_id;
	}

	@JsonProperty("debata_id")
	public String getDebata_id() {
		return debata_id;
	}

	@JsonProperty("debata_id")
	public void setDebata_id(String debata_id) {
		this.debata_id = debata_id;
	}

	@JsonProperty("dlugosc")
	public String getDlugosc() {
		return dlugosc;
	}

	@JsonProperty("dlugosc")
	public void setDlugosc(String dlugosc) {
		this.dlugosc = dlugosc;
	}

	@JsonProperty("dzien_sejmowy_id")
	public String getDzien_sejmowy_id() {
		return dzien_sejmowy_id;
	}

	@JsonProperty("dzien_sejmowy_id")
	public void setDzien_sejmowy_id(String dzien_sejmowy_id) {
		this.dzien_sejmowy_id = dzien_sejmowy_id;
	}

	@JsonProperty("ilosc_slow")
	public String getIlosc_slow() {
		return ilosc_slow;
	}

	@JsonProperty("ilosc_slow")
	public void setIlosc_slow(String ilosc_slow) {
		this.ilosc_slow = ilosc_slow;
	}

	@JsonProperty("klub_id")
	public String getKlub_id() {
		return klub_id;
	}

	@JsonProperty("klub_id")
	public void setKlub_id(String klub_id) {
		this.klub_id = klub_id;
	}

	@JsonProperty("kolejnosc")
	public Long getKolejnosc() {
		return kolejnosc;
	}

	@JsonProperty("kolejnosc")
	public void setKolejnosc(Long kolejnosc) {
		this.kolejnosc = kolejnosc;
	}

	@JsonProperty("punkt_id")
	public String getPunkt_id() {
		return punkt_id;
	}

	@JsonProperty("punkt_id")
	public void setPunkt_id(String punkt_id) {
		this.punkt_id = punkt_id;
	}

	@JsonProperty("skrot")
	public String getSkrot() {
		return skrot;
	}

	@JsonProperty("skrot")
	public void setSkrot(String skrot) {
		this.skrot = skrot;
	}

	@JsonProperty("stanowisko_id")
	public String getStanowisko_id() {
		return stanowisko_id;
	}

	@JsonProperty("stanowisko_id")
	public void setStanowisko_id(String stanowisko_id) {
		this.stanowisko_id = stanowisko_id;
	}

	@JsonProperty("video")
	public String getVideo() {
		return video;
	}

	@JsonProperty("video")
	public void setVideo(String video) {
		this.video = video;
	}

	@JsonProperty("yt_id")
	public String getYt_id() {
		return yt_id;
	}

	@JsonProperty("yt_id")
	public void setYt_id(String yt_id) {
		this.yt_id = yt_id;
	}

	@JsonProperty("yt_pl_id")
	public String getYt_pl_id() {
		return yt_pl_id;
	}

	@JsonProperty("yt_pl_id")
	public void setYt_pl_id(String yt_pl_id) {
		this.yt_pl_id = yt_pl_id;
	}

	@JsonProperty("stanowiska.id")
	public String getStanowiska_id() {
		return stanowiska_id;
	}

	@JsonProperty("stanowiska.id")
	public void setStanowiska_id(String stanowiska_id) {
		this.stanowiska_id = stanowiska_id;
	}

	@JsonProperty("stanowiska.nazwa")
	public String getStanowiska_nazwa() {
		return stanowiska_nazwa;
	}

	@JsonProperty("stanowiska.nazwa")
	public void setStanowiska_nazwa(String stanowiska_nazwa) {
		this.stanowiska_nazwa = stanowiska_nazwa;
	}

	@JsonProperty("tytul")
	public String getTytul() {
		return tytul;
	}

	@JsonProperty("tytul")
	public void setTytul(String tytul) {
		this.tytul = tytul;
	}

	@JsonProperty("stanowisko_mowca")
	public String getStanowisko_mowca() {
		return stanowisko_mowca;
	}

	@JsonProperty("stanowisko_mowca")
	public void setStanowisko_mowca(String stanowisko_mowca) {
		this.stanowisko_mowca = stanowisko_mowca;
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
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
