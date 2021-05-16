package com.stronans.domotics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable class describing a Domotic Station and what kind of sensor it is using.
 * Created by S.King on 11/07/2016.
 */
public final class Station {
    private final String stationId;
    private final String cluster;
    private final Integer address;
    private final String name;
    private final String description;
    private final String sensorType;

    @JsonCreator
    public Station(String stationId, String cluster, Integer address, String name, String description, String sensorType) {
        this.stationId = stationId;
        this.cluster = cluster;
        this.address = address;
        this.name = name;
        this.description = description;
        this.sensorType = sensorType;
    }

    @JsonProperty("id")
    public String StationId() {
        return stationId;
    }

    @JsonProperty("cluster")
    public String Cluster() {
        return cluster;
    }

    @JsonProperty("address")
    public Integer Address() {
        return address;
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
