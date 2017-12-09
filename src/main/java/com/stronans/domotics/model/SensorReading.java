package com.stronans.domotics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Default model of data gathered from a measurement sersor. Note that there is not time associated with this model as
 * most sensors do not provide this. Original designed for what comes in from the DHT22/ESP8622 station. Changed so that
 * the values are anonymous so that there is no pre-defined bias to what the values represent.
 *
 * This is a POJO so that the JSON can be set automagically by Spring Web
 *
 * Created by S.King on 04/07/2016.
 * Updated by S.King on 10/06/2017 to anonymous the measurement names.
 *
 */
public class SensorReading {
    private long stationId;
    private double value1;
    private double value2;
    private double value3;
    private int sampleRate;
    private int sensorType;

    public SensorReading() {
    }

    @JsonCreator
    public SensorReading(
            @JsonProperty("stationId") long stationId,
            @JsonProperty("temperatureValue") double value1,
            @JsonProperty("humidityValue") double value2,
            @JsonProperty("humitureValue") double value3,
            @JsonProperty("sampleRate") int sampleRate,
            @JsonProperty("sensorType") int sensorType) {
        this.stationId = stationId;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.sampleRate = sampleRate;
        this.sensorType = sensorType;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public double getValue1() {
        return value1;
    }

    public void setValue1(double value1) {
        this.value1 = value1;
    }

    public double getValue2() {
        return value2;
    }

    public void setValue2(double value2) {
        this.value2 = value2;
    }

    public double getValue3() {
        return value3;
    }

    public void setValue3(double value3) {
        this.value3 = value3;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getSensorType() {
        return sensorType;
    }

    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }
}
