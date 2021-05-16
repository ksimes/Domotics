package com.stronans.domotics.model.sensors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stronans.domotics.model.measurements.MissingAttributeException;
import lombok.Getter;

import java.util.Map;

@Getter
public class SensorSingleReading {
    @JsonProperty("address")
    private Integer address;
    @JsonProperty("measureType")
    private String measureType;
    @JsonProperty("sensorType")
    private String sensorType;
    @JsonProperty("sampleRate")
    private Integer sampleRate;
    @JsonProperty("payload")
    private Map<String, Object> payload;

    public SensorSingleReading() {
    }

    public SensorSingleReading(Integer addressId, String measureType, String sensorType, Integer sampleRate, Map<String, Object> payload) {
        this.address = addressId;
        this.measureType = measureType;
        this.sensorType = sensorType;
        this.sampleRate = sampleRate;
        this.payload = payload;
    }

    private void validateReading(SensorSingleReading reading) {
        if (reading.address == null || reading.measureType == null || reading.sensorType == null || reading.payload == null) {
            throw new MissingAttributeException("address or measureType or sensorType or payload attributes missing from JSON message");
        }
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "address=" + address +
                ", measureType='" + measureType + '\'' +
                ", sensorType='" + sensorType + '\'' +
                ", sampleRate=" + sampleRate +
                ", payload=" + payload +
                '}';
    }
}
