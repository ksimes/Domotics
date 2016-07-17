package com.stronans.domotics.model;

/**
 * This is what comes in from the DHT22/ESP8622 station.
 * This is a POJO so that the JSON can be set automagically by Spring Web
 *
 * Created by S.King on 04/07/2016.
 */
public class Reading {
    private long stationId;
    private double temperatureValue;
    private double humidityValue;
    private double humitureValue;

    public Reading() {
    }

    public Reading(long stationId, double temperatureValue, double humidityValue, double humitureValue) {
        this.stationId = stationId;
        this.temperatureValue = temperatureValue;
        this.humidityValue = humidityValue;
        this.humitureValue = humitureValue;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public double getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(double temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public double getHumidityValue() {
        return humidityValue;
    }

    public void setHumidityValue(double humidityValue) {
        this.humidityValue = humidityValue;
    }

    public double getHumitureValue() {
        return humitureValue;
    }

    public void setHumitureValue(double humitureValue) {
        this.humitureValue = humitureValue;
    }
}
