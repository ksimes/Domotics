package com.stronans.domotics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable class describing a Domotic Station and what kind of sensor it is using.
 * Created by S.King on 11/07/2016.
 */
public final class Station
{
    private final String stationId;
    private final String name;
    private final String description;
    private final String sensorType;

    @JsonCreator
    public Station(String stationId, String name, String description, String sensorType) {
        this.stationId = stationId;
        this.name = name;
        this.description = description;
        this.sensorType = sensorType;
    }

    @JsonProperty("_key")
    public String StationId() {
        return stationId;
    }

    @JsonProperty("name")
    public String Name() {
        return name;
    }

    @JsonProperty("description")
    public String Description() {
        return description;
    }

    @JsonProperty("sensorType")
    public String sensorType() {
        return sensorType;
    }
}
