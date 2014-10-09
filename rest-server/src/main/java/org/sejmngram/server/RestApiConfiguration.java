package org.sejmngram.server;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;

public class RestApiConfiguration extends Configuration {

	@Valid
    @NotNull
    @JsonProperty
    private DatabaseConfiguration database = new DatabaseConfiguration();

    @NotEmpty
    @JsonProperty
	private String poselIdFilename;

	@NotEmpty
    @JsonProperty
	private String partiaIdFilename;

	@JsonProperty
	private String redisAddress;

    public String getPoselIdFilename() {
    	return poselIdFilename;
    }

    public String getPartiaIdFilename() {
    	return partiaIdFilename;
    }
    
    public DatabaseConfiguration getDatabaseConfiguration() {
        return database;
    }

	public String getRedisAddress() {
		return redisAddress;
	}
}
