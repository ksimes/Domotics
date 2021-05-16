package com.stronans.domotics.model.sensors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class SensorCache {
    private final String stationId;
    private final double value1;
    private final double value2;
    private final double value3;
    private final int sampleRate;
    private final String sensorType;
    private final String name;
    private final String description;
    private final String timeStamp;

    @JsonCreator
    public SensorCache(
            @JsonProperty("stationId") String stationId,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("timeStamp") String timeStamp,
            @JsonProperty("temperature") double value1,
            @JsonProperty("humidity") double value2,
            @JsonProperty("heatIndex") double value3,
            @JsonProperty("sampleRate") int sampleRate,
            @JsonProperty("sensorType") String sensorType) {
        this.name = name;
        this.description = description;
        this.stationId = stationId;
        this.timeStamp = timeStamp;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.sampleRate = sampleRate;
        this.sensorType = sensorType;
    }

    @JsonProperty("name")
    public String name() {
        return name;
    }

    @JsonProperty("description")
    public String description() {
        return description;
    }

    @JsonProperty("timeStamp")
    public String timeStamp() {
        return timeStamp;
    }

    @JsonProperty("sensorType")
    public String sensorType() {
        return sensorType;
    }

    @JsonProperty("stationId")
    public String stationId() {
        return stationId;
    }

    @JsonProperty("temperature")
    public double value1() {
        return value1;
    }

    @JsonProperty("humidity")
    public double value2() {
        return value2;
    }

    @JsonProperty("heatIndex")
    public double value3() {
        return value3;
    }

    @JsonProperty("sampleRate")
    public int sampleRate() {
        return sampleRate;
    }
}
