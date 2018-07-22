package com.stronans.domotics.model;

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

    @JsonCreator
    public SensorCache(
            @JsonProperty("stationId") String stationId,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("temperatureValue") double value1,
            @JsonProperty("humidityValue") double value2,
            @JsonProperty("humitureValue") double value3,
            @JsonProperty("sampleRate") int sampleRate,
            @JsonProperty("sensorType") String sensorType) {
        this.name = name;
        this.description = description;
        this.stationId = stationId;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.sampleRate = sampleRate;
        this.sensorType = sensorType;
    }

    public String Name() {
        return name;
    }

    public String Description() {
        return description;
    }

    public String sensorType() {
        return sensorType;
    }

    public String stationId() {
        return stationId;
    }

    public double value1() {
        return value1;
    }

    public double value2() {
        return value2;
    }

    public double value3() {
        return value3;
    }

    public int sampleRate() {
        return sampleRate;
    }
}
