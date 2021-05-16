package com.stronans.domotics.model.sensors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable class describing a kind of sensor and what information it gathers.
 * Created by S.King on 06/08/2016.
 */
public final class SensorType {
    private final String id;
    private final String name;
    private final String description;

    @JsonCreator
    public SensorType(String sensorId, String name, String description) {
        this.id = sensorId;
        this.name = name;
        this.description = description;
    }

    @JsonProperty("id")
    public String sensorId() {
        return id;
    }

    @JsonProperty("name")
    public String Name() {
        return name;
    }

    @JsonProperty("description")
    public String Description() {
        return description;
    }
}
