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
@JsonPropertyOrder({ "_dataset", "_object_id", "_data", "_layers" })
public class KlubResponse {

	@JsonProperty("_dataset")
	private String _dataset;
	@JsonProperty("_object_id")
	private String _object_id;
	@JsonProperty("_data")
	private Data _data;
	@JsonProperty("_layers")
	private Layers _layers;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("_dataset")
	public String get_dataset() {
		return _dataset;
	}

	@JsonProperty("_dataset")
	public void set_dataset(String _dataset) {
		this._dataset = _dataset;
	}

	@JsonProperty("_object_id")
	public String get_object_id() {
		return _object_id;
	}

	@JsonProperty("_object_id")
	public void set_object_id(String _object_id) {
		this._object_id = _object_id;
	}

	@JsonProperty("_data")
	public Data get_data() {
		return _data;
	}

	@JsonProperty("_data")
	public void set_data(Data _data) {
		this._data = _data;
	}

	@JsonProperty("_layers")
	public Layers get_layers() {
		return _layers;
	}

	@JsonProperty("_layers")
	public void set_layers(Layers _layers) {
		this._layers = _layers;
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
