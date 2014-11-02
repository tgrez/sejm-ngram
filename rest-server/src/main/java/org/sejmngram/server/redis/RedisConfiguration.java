package org.sejmngram.server.redis;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class RedisConfiguration {

    @NotEmpty
    @JsonProperty
    private String host;

    @Min(1)
    @Max(65535)
    @JsonProperty
    private int port;

    @JsonProperty
    private String maxMemory = "1000000000"; // in bytes for redis 2.8.13

    @JsonProperty
    private String maxMemoryPolicy = "volatile-lru";

    @Min(1)
    @JsonProperty
    private int maxMemorySamples = 5;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getMaxMemory() {
        return maxMemory;
    }

    public String getMaxMemoryPolicy() {
        return maxMemoryPolicy;
    }

    public int getMaxMemorySamples() {
        return maxMemorySamples;
    }
}
