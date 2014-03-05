package org.sejmngram.common.json.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "wystapienia" })
public class Dokument {

	@JsonProperty("wystapienia")
	private List<Wystapienie> wystapienia = new ArrayList<Wystapienie>();
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("wystapienia")
	public List<Wystapienie> getWystapienia() {
		return wystapienia;
	}

	@JsonProperty("wystapienia")
	public void setWystapienia(List<Wystapienie> wystapienia) {
		this.wystapienia = wystapienia;
	}
	
	public void addWystapienia(List<Wystapienie> wystapienia) {
		this.wystapienia.addAll(wystapienia);
	}
	
	public void addWystapienie(Wystapienie wystapienie) {
		wystapienia.add(wystapienie);
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