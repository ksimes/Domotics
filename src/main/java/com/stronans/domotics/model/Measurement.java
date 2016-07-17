package com.stronans.domotics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stronans.domotics.utilities.DateInfo;

/**
 * Immutable model of measurement value from a specific station.
 * This model will suit all current measurements taken.
 * Created by S.King on 03/07/2016.
 */
public final class Measurement {
    private Long id;
    private final long stationId;
    private final double value;
    private final DateInfo timeStamp;

    public Measurement(long stationId, double value, String timeStamp) {
        this.id = null;
        this.stationId = stationId;
        this.timeStamp = DateInfo.fromUniversalString(timeStamp);
        this.value = value;
    }

    @JsonCreator
    public Measurement(Long id, long stationId, double value, String timeStamp) {
        this.id = id;
        this.stationId = stationId;
        this.timeStamp = DateInfo.fromUniversalString(timeStamp);
        this.value = value;
    }

    public Measurement(Long id, long stationId, double value, DateInfo timeStamp) {
        this.id = id;
        this.stationId = stationId;
        this.timeStamp = timeStamp;
        this.value = value;
    }

    public Measurement(long stationId, double value, DateInfo timeStamp) {
        this(null, stationId, value, timeStamp);
    }

    public Measurement setId(Long id) {
        return new Measurement(id, this.stationId, this.value, this.timeStamp);
    }

    @JsonProperty("id")
    public Long id() {
        return id;
    }

    @JsonProperty("value")
    public double value() {
        return value;
    }

    @JsonProperty("timestamp")
    public String timeStampString() {
        return DateInfo.toUniversalString(timeStamp);
    }

    public DateInfo timeStamp() {
        return timeStamp;
    }

    @JsonProperty("stationId")
    public long stationId() {
        return stationId;
    }
}
