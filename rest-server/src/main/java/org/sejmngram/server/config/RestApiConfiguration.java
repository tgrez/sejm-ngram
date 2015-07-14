package org.sejmngram.server.config;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.elasticsearch.config.EsConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.sejmngram.server.redis.RedisConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestApiConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty
    private RedisConfiguration redis;

    @JsonProperty
    private EsConfiguration elasticsearch;

    @NotEmpty
    @JsonProperty
    private String poselIdFilename;

    @NotEmpty
    @JsonProperty
    private String partiaIdFilename;

    @NotEmpty
    @JsonProperty
    private String datesFilename;

    public String getPoselIdFilename() {
        return poselIdFilename;
    }

    public String getPartiaIdFilename() {
        return partiaIdFilename;
    }

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public RedisConfiguration getRedis() {
        return redis;
    }

    public EsConfiguration getElasticsearch() {
        return elasticsearch;
    }

    public String getDatesFilename() {
        return datesFilename;
    }

    public SejmFilesConfiguration getSejmFilesConfig() {
        return new SejmFilesConfiguration(getPartiaIdFilename(),
                getPoselIdFilename(), getDatesFilename());
    }
}
