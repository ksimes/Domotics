package com.stronans.domotics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by S.King on 14/09/2014.
 */
public class ErrorInfo {
    @JsonProperty("url")
    public final String url;
    @JsonProperty("exception")
    public final String ex;

    @JsonCreator
    public ErrorInfo(@JsonProperty("url") String url, Exception ex) {
        this.url = url;
        this.ex = ex.getLocalizedMessage();
    }
}
