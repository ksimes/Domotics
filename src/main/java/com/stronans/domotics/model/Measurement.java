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
    private final int sampleRate;
    private final boolean status;
    private final int sensorType;

    public Measurement(Long id, long stationId, double value, DateInfo timeStamp, int sampleRate, int sensorType, boolean status) {
        this.id = id;
        this.stationId = stationId;
        this.timeStamp = timeStamp;
        this.value = value;
        this.sampleRate = sampleRate;
        this.status = status;
        this.sensorType = sensorType;
    }

    @JsonCreator
    public Measurement(Long id, long stationId, double value, String timeStamp, int sampleRate, int sensorType) {
        this(id, stationId, value, DateInfo.fromUniversalString(timeStamp), sampleRate, sensorType, true);
    }

    public Measurement(Long id, long stationId, double value, DateInfo timeStamp, int sampleRate, int sensorType) {
        this(id, stationId, value, timeStamp, sampleRate, sensorType, true);
    }

    public Measurement(long stationId, double value, DateInfo timeStamp, int sampleRate, int sensorType) {
        this(null, stationId, value, timeStamp, sampleRate, sensorType);
    }

    public Measurement setId(Long id) {
        return new Measurement(id, this.stationId, this.value, this.timeStamp, this.sampleRate, this.sensorType);
    }

    public Measurement setDummy(long stationId, DateInfo timeStamp) {
        return new Measurement(null, stationId, 0.0, timeStamp, 0, 0, false);
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

    @JsonProperty("sampleRate")
    public int sampleRate() {
        return sampleRate;
    }

    @JsonProperty("status")
    public boolean status() {
        return status;
    }

    @JsonProperty("sensorType")
    public int sensorType() {
        return sensorType;
    }
}
