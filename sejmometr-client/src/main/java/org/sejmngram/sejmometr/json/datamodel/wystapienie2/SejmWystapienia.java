package org.sejmngram.sejmometr.json.datamodel.wystapienie2;

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
@JsonPropertyOrder({ "id", "posiedzenie_id", "stenogram_id", "punkt_id",
		"subpunkt_id", "i", "mowca_str", "o_txt", "m_txt", "p_txt", "skrot",
		"ilosc_slow", "mowca_funkcja_id", "mowca_id", "klub_id", "marszalek",
		"posel", "analiza", "analiza_ts", "akcept", "video_blok_id",
		"video_przerwa_id", "video_start", "video_stop", "video",
		"video_analiza", "video_analiza_ts", "video_poster",
		"video_post_analiza", "video_post_analiza_ts", "duration_int",
		"duration_str", "datetime_start", "datetime_stop", "time_start",
		"time_stop", "timing_analiza", "timing_analiza_ts", "timing",
		"ogv_size", "mp4_size", "ogladalnosc", "analiza_slowa",
		"analiza_slowa_ts", "kolejnosc", "analiza_kolejnosc",
		"analiza_kolejnosc_ts", "yt", "yt_ts", "yt_pl", "yt_pl_ts",
		"yt_pl_set", "posiedzenie_i", "yt_id", "yt_pl_id", "s3", "s3_mpg",
		"deleted", "playlist_id", "playlist_position" })
public class SejmWystapienia {

	@JsonProperty("id")
	private String id;
	@JsonProperty("posiedzenie_id")
	private String posiedzenie_id;
	@JsonProperty("stenogram_id")
	private String stenogram_id;
	@JsonProperty("punkt_id")
	private String punkt_id;
	@JsonProperty("subpunkt_id")
	private String subpunkt_id;
	@JsonProperty("i")
	private String i;
	@JsonProperty("mowca_str")
	private String mowca_str;
	@JsonProperty("o_txt")
	private String o_txt;
	@JsonProperty("m_txt")
	private String m_txt;
	@JsonProperty("p_txt")
	private String p_txt;
	@JsonProperty("skrot")
	private String skrot;
	@JsonProperty("ilosc_slow")
	private String ilosc_slow;
	@JsonProperty("mowca_funkcja_id")
	private String mowca_funkcja_id;
	@JsonProperty("mowca_id")
	private String mowca_id;
	@JsonProperty("klub_id")
	private String klub_id;
	@JsonProperty("marszalek")
	private String marszalek;
	@JsonProperty("posel")
	private String posel;
	@JsonProperty("analiza")
	private String analiza;
	@JsonProperty("analiza_ts")
	private String analiza_ts;
	@JsonProperty("akcept")
	private String akcept;
	@JsonProperty("video_blok_id")
	private String video_blok_id;
	@JsonProperty("video_przerwa_id")
	private String video_przerwa_id;
	@JsonProperty("video_start")
	private String video_start;
	@JsonProperty("video_stop")
	private String video_stop;
	@JsonProperty("video")
	private String video;
	@JsonProperty("video_analiza")
	private String video_analiza;
	@JsonProperty("video_analiza_ts")
	private String video_analiza_ts;
	@JsonProperty("video_poster")
	private String video_poster;
	@JsonProperty("video_post_analiza")
	private String video_post_analiza;
	@JsonProperty("video_post_analiza_ts")
	private String video_post_analiza_ts;
	@JsonProperty("duration_int")
	private String duration_int;
	@JsonProperty("duration_str")
	private String duration_str;
	@JsonProperty("datetime_start")
	private String datetime_start;
	@JsonProperty("datetime_stop")
	private String datetime_stop;
	@JsonProperty("time_start")
	private String time_start;
	@JsonProperty("time_stop")
	private String time_stop;
	@JsonProperty("timing_analiza")
	private String timing_analiza;
	@JsonProperty("timing_analiza_ts")
	private String timing_analiza_ts;
	@JsonProperty("timing")
	private String timing;
	@JsonProperty("ogv_size")
	private String ogv_size;
	@JsonProperty("mp4_size")
	private String mp4_size;
	@JsonProperty("ogladalnosc")
	private String ogladalnosc;
	@JsonProperty("analiza_slowa")
	private String analiza_slowa;
	@JsonProperty("analiza_slowa_ts")
	private String analiza_slowa_ts;
	@JsonProperty("kolejnosc")
	private String kolejnosc;
	@JsonProperty("analiza_kolejnosc")
	private String analiza_kolejnosc;
	@JsonProperty("analiza_kolejnosc_ts")
	private String analiza_kolejnosc_ts;
	@JsonProperty("yt")
	private String yt;
	@JsonProperty("yt_ts")
	private String yt_ts;
	@JsonProperty("yt_pl")
	private String yt_pl;
	@JsonProperty("yt_pl_ts")
	private String yt_pl_ts;
	@JsonProperty("yt_pl_set")
	private String yt_pl_set;
	@JsonProperty("posiedzenie_i")
	private String posiedzenie_i;
	@JsonProperty("yt_id")
	private String yt_id;
	@JsonProperty("yt_pl_id")
	private String yt_pl_id;
	@JsonProperty("s3")
	private String s3;
	@JsonProperty("s3_mpg")
	private String s3_mpg;
	@JsonProperty("deleted")
	private String deleted;
	@JsonProperty("playlist_id")
	private String playlist_id;
	@JsonProperty("playlist_position")
	private String playlist_position;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

	@JsonProperty("stenogram_id")
	public String getStenogram_id() {
		return stenogram_id;
	}

	@JsonProperty("stenogram_id")
	public void setStenogram_id(String stenogram_id) {
		this.stenogram_id = stenogram_id;
	}

	@JsonProperty("punkt_id")
	public String getPunkt_id() {
		return punkt_id;
	}

	@JsonProperty("punkt_id")
	public void setPunkt_id(String punkt_id) {
		this.punkt_id = punkt_id;
	}

	@JsonProperty("subpunkt_id")
	public String getSubpunkt_id() {
		return subpunkt_id;
	}

	@JsonProperty("subpunkt_id")
	public void setSubpunkt_id(String subpunkt_id) {
		this.subpunkt_id = subpunkt_id;
	}

	@JsonProperty("i")
	public String getI() {
		return i;
	}

	@JsonProperty("i")
	public void setI(String i) {
		this.i = i;
	}

	@JsonProperty("mowca_str")
	public String getMowca_str() {
		return mowca_str;
	}

	@JsonProperty("mowca_str")
	public void setMowca_str(String mowca_str) {
		this.mowca_str = mowca_str;
	}

	@JsonProperty("o_txt")
	public String getO_txt() {
		return o_txt;
	}

	@JsonProperty("o_txt")
	public void setO_txt(String o_txt) {
		this.o_txt = o_txt;
	}

	@JsonProperty("m_txt")
	public String getM_txt() {
		return m_txt;
	}

	@JsonProperty("m_txt")
	public void setM_txt(String m_txt) {
		this.m_txt = m_txt;
	}

	@JsonProperty("p_txt")
	public String getP_txt() {
		return p_txt;
	}

	@JsonProperty("p_txt")
	public void setP_txt(String p_txt) {
		this.p_txt = p_txt;
	}

	@JsonProperty("skrot")
	public String getSkrot() {
		return skrot;
	}

	@JsonProperty("skrot")
	public void setSkrot(String skrot) {
		this.skrot = skrot;
	}

	@JsonProperty("ilosc_slow")
	public String getIlosc_slow() {
		return ilosc_slow;
	}

	@JsonProperty("ilosc_slow")
	public void setIlosc_slow(String ilosc_slow) {
		this.ilosc_slow = ilosc_slow;
	}

	@JsonProperty("mowca_funkcja_id")
	public String getMowca_funkcja_id() {
		return mowca_funkcja_id;
	}

	@JsonProperty("mowca_funkcja_id")
	public void setMowca_funkcja_id(String mowca_funkcja_id) {
		this.mowca_funkcja_id = mowca_funkcja_id;
	}

	@JsonProperty("mowca_id")
	public String getMowca_id() {
		return mowca_id;
	}

	@JsonProperty("mowca_id")
	public void setMowca_id(String mowca_id) {
		this.mowca_id = mowca_id;
	}

	@JsonProperty("klub_id")
	public String getKlub_id() {
		return klub_id;
	}

	@JsonProperty("klub_id")
	public void setKlub_id(String klub_id) {
		this.klub_id = klub_id;
	}

	@JsonProperty("marszalek")
	public String getMarszalek() {
		return marszalek;
	}

	@JsonProperty("marszalek")
	public void setMarszalek(String marszalek) {
		this.marszalek = marszalek;
	}

	@JsonProperty("posel")
	public String getPosel() {
		return posel;
	}

	@JsonProperty("posel")
	public void setPosel(String posel) {
		this.posel = posel;
	}

	@JsonProperty("analiza")
	public String getAnaliza() {
		return analiza;
	}

	@JsonProperty("analiza")
	public void setAnaliza(String analiza) {
		this.analiza = analiza;
	}

	@JsonProperty("analiza_ts")
	public String getAnaliza_ts() {
		return analiza_ts;
	}

	@JsonProperty("analiza_ts")
	public void setAnaliza_ts(String analiza_ts) {
		this.analiza_ts = analiza_ts;
	}

	@JsonProperty("akcept")
	public String getAkcept() {
		return akcept;
	}

	@JsonProperty("akcept")
	public void setAkcept(String akcept) {
		this.akcept = akcept;
	}

	@JsonProperty("video_blok_id")
	public String getVideo_blok_id() {
		return video_blok_id;
	}

	@JsonProperty("video_blok_id")
	public void setVideo_blok_id(String video_blok_id) {
		this.video_blok_id = video_blok_id;
	}

	@JsonProperty("video_przerwa_id")
	public String getVideo_przerwa_id() {
		return video_przerwa_id;
	}

	@JsonProperty("video_przerwa_id")
	public void setVideo_przerwa_id(String video_przerwa_id) {
		this.video_przerwa_id = video_przerwa_id;
	}

	@JsonProperty("video_start")
	public String getVideo_start() {
		return video_start;
	}

	@JsonProperty("video_start")
	public void setVideo_start(String video_start) {
		this.video_start = video_start;
	}

	@JsonProperty("video_stop")
	public String getVideo_stop() {
		return video_stop;
	}

	@JsonProperty("video_stop")
	public void setVideo_stop(String video_stop) {
		this.video_stop = video_stop;
	}

	@JsonProperty("video")
	public String getVideo() {
		return video;
	}

	@JsonProperty("video")
	public void setVideo(String video) {
		this.video = video;
	}

	@JsonProperty("video_analiza")
	public String getVideo_analiza() {
		return video_analiza;
	}

	@JsonProperty("video_analiza")
	public void setVideo_analiza(String video_analiza) {
		this.video_analiza = video_analiza;
	}

	@JsonProperty("video_analiza_ts")
	public String getVideo_analiza_ts() {
		return video_analiza_ts;
	}

	@JsonProperty("video_analiza_ts")
	public void setVideo_analiza_ts(String video_analiza_ts) {
		this.video_analiza_ts = video_analiza_ts;
	}

	@JsonProperty("video_poster")
	public String getVideo_poster() {
		return video_poster;
	}

	@JsonProperty("video_poster")
	public void setVideo_poster(String video_poster) {
		this.video_poster = video_poster;
	}

	@JsonProperty("video_post_analiza")
	public String getVideo_post_analiza() {
		return video_post_analiza;
	}

	@JsonProperty("video_post_analiza")
	public void setVideo_post_analiza(String video_post_analiza) {
		this.video_post_analiza = video_post_analiza;
	}

	@JsonProperty("video_post_analiza_ts")
	public String getVideo_post_analiza_ts() {
		return video_post_analiza_ts;
	}

	@JsonProperty("video_post_analiza_ts")
	public void setVideo_post_analiza_ts(String video_post_analiza_ts) {
		this.video_post_analiza_ts = video_post_analiza_ts;
	}

	@JsonProperty("duration_int")
	public String getDuration_int() {
		return duration_int;
	}

	@JsonProperty("duration_int")
	public void setDuration_int(String duration_int) {
		this.duration_int = duration_int;
	}

	@JsonProperty("duration_str")
	public String getDuration_str() {
		return duration_str;
	}

	@JsonProperty("duration_str")
	public void setDuration_str(String duration_str) {
		this.duration_str = duration_str;
	}

	@JsonProperty("datetime_start")
	public String getDatetime_start() {
		return datetime_start;
	}

	@JsonProperty("datetime_start")
	public void setDatetime_start(String datetime_start) {
		this.datetime_start = datetime_start;
	}

	@JsonProperty("datetime_stop")
	public String getDatetime_stop() {
		return datetime_stop;
	}

	@JsonProperty("datetime_stop")
	public void setDatetime_stop(String datetime_stop) {
		this.datetime_stop = datetime_stop;
	}

	@JsonProperty("time_start")
	public String getTime_start() {
		return time_start;
	}

	@JsonProperty("time_start")
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	@JsonProperty("time_stop")
	public String getTime_stop() {
		return time_stop;
	}

	@JsonProperty("time_stop")
	public void setTime_stop(String time_stop) {
		this.time_stop = time_stop;
	}

	@JsonProperty("timing_analiza")
	public String getTiming_analiza() {
		return timing_analiza;
	}

	@JsonProperty("timing_analiza")
	public void setTiming_analiza(String timing_analiza) {
		this.timing_analiza = timing_analiza;
	}

	@JsonProperty("timing_analiza_ts")
	public String getTiming_analiza_ts() {
		return timing_analiza_ts;
	}

	@JsonProperty("timing_analiza_ts")
	public void setTiming_analiza_ts(String timing_analiza_ts) {
		this.timing_analiza_ts = timing_analiza_ts;
	}

	@JsonProperty("timing")
	public String getTiming() {
		return timing;
	}

	@JsonProperty("timing")
	public void setTiming(String timing) {
		this.timing = timing;
	}

	@JsonProperty("ogv_size")
	public String getOgv_size() {
		return ogv_size;
	}

	@JsonProperty("ogv_size")
	public void setOgv_size(String ogv_size) {
		this.ogv_size = ogv_size;
	}

	@JsonProperty("mp4_size")
	public String getMp4_size() {
		return mp4_size;
	}

	@JsonProperty("mp4_size")
	public void setMp4_size(String mp4_size) {
		this.mp4_size = mp4_size;
	}

	@JsonProperty("ogladalnosc")
	public String getOgladalnosc() {
		return ogladalnosc;
	}

	@JsonProperty("ogladalnosc")
	public void setOgladalnosc(String ogladalnosc) {
		this.ogladalnosc = ogladalnosc;
	}

	@JsonProperty("analiza_slowa")
	public String getAnaliza_slowa() {
		return analiza_slowa;
	}

	@JsonProperty("analiza_slowa")
	public void setAnaliza_slowa(String analiza_slowa) {
		this.analiza_slowa = analiza_slowa;
	}

	@JsonProperty("analiza_slowa_ts")
	public String getAnaliza_slowa_ts() {
		return analiza_slowa_ts;
	}

	@JsonProperty("analiza_slowa_ts")
	public void setAnaliza_slowa_ts(String analiza_slowa_ts) {
		this.analiza_slowa_ts = analiza_slowa_ts;
	}

	@JsonProperty("kolejnosc")
	public String getKolejnosc() {
		return kolejnosc;
	}

	@JsonProperty("kolejnosc")
	public void setKolejnosc(String kolejnosc) {
		this.kolejnosc = kolejnosc;
	}

	@JsonProperty("analiza_kolejnosc")
	public String getAnaliza_kolejnosc() {
		return analiza_kolejnosc;
	}

	@JsonProperty("analiza_kolejnosc")
	public void setAnaliza_kolejnosc(String analiza_kolejnosc) {
		this.analiza_kolejnosc = analiza_kolejnosc;
	}

	@JsonProperty("analiza_kolejnosc_ts")
	public String getAnaliza_kolejnosc_ts() {
		return analiza_kolejnosc_ts;
	}

	@JsonProperty("analiza_kolejnosc_ts")
	public void setAnaliza_kolejnosc_ts(String analiza_kolejnosc_ts) {
		this.analiza_kolejnosc_ts = analiza_kolejnosc_ts;
	}

	@JsonProperty("yt")
	public String getYt() {
		return yt;
	}

	@JsonProperty("yt")
	public void setYt(String yt) {
		this.yt = yt;
	}

	@JsonProperty("yt_ts")
	public String getYt_ts() {
		return yt_ts;
	}

	@JsonProperty("yt_ts")
	public void setYt_ts(String yt_ts) {
		this.yt_ts = yt_ts;
	}

	@JsonProperty("yt_pl")
	public String getYt_pl() {
		return yt_pl;
	}

	@JsonProperty("yt_pl")
	public void setYt_pl(String yt_pl) {
		this.yt_pl = yt_pl;
	}

	@JsonProperty("yt_pl_ts")
	public String getYt_pl_ts() {
		return yt_pl_ts;
	}

	@JsonProperty("yt_pl_ts")
	public void setYt_pl_ts(String yt_pl_ts) {
		this.yt_pl_ts = yt_pl_ts;
	}

	@JsonProperty("yt_pl_set")
	public String getYt_pl_set() {
		return yt_pl_set;
	}

	@JsonProperty("yt_pl_set")
	public void setYt_pl_set(String yt_pl_set) {
		this.yt_pl_set = yt_pl_set;
	}

	@JsonProperty("posiedzenie_i")
	public String getPosiedzenie_i() {
		return posiedzenie_i;
	}

	@JsonProperty("posiedzenie_i")
	public void setPosiedzenie_i(String posiedzenie_i) {
		this.posiedzenie_i = posiedzenie_i;
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

	@JsonProperty("s3")
	public String getS3() {
		return s3;
	}

	@JsonProperty("s3")
	public void setS3(String s3) {
		this.s3 = s3;
	}

	@JsonProperty("s3_mpg")
	public String getS3_mpg() {
		return s3_mpg;
	}

	@JsonProperty("s3_mpg")
	public void setS3_mpg(String s3_mpg) {
		this.s3_mpg = s3_mpg;
	}

	@JsonProperty("deleted")
	public String getDeleted() {
		return deleted;
	}

	@JsonProperty("deleted")
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	@JsonProperty("playlist_id")
	public String getPlaylist_id() {
		return playlist_id;
	}

	@JsonProperty("playlist_id")
	public void setPlaylist_id(String playlist_id) {
		this.playlist_id = playlist_id;
	}

	@JsonProperty("playlist_position")
	public String getPlaylist_position() {
		return playlist_position;
	}

	@JsonProperty("playlist_position")
	public void setPlaylist_position(String playlist_position) {
		this.playlist_position = playlist_position;
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
