package org.sejmngram.sejmometr.json.datamodel.klub2;

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
@JsonPropertyOrder({ "score", "related" })
public class Layers {

	@JsonProperty("score")
	private Score score;
	@JsonProperty("related")
	private Boolean related;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("score")
	public Score getScore() {
		return score;
	}

	@JsonProperty("score")
	public void setScore(Score score) {
		this.score = score;
	}

	@JsonProperty("related")
	public Boolean getRelated() {
		return related;
	}

	@JsonProperty("related")
	public void setRelated(Boolean related) {
		this.related = related;
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