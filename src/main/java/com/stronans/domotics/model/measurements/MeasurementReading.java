package com.stronans.domotics.model.measurements;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stronans.domotics.model.sensors.SensorRoot;
import com.stronans.domotics.model.sensors.SensorSingleReading;
import com.stronans.domotics.utilities.DateInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeasurementReading extends MeasurementRoot {
    @JsonProperty("address")
    protected Integer addressId;
    @JsonProperty("measureType")
    protected String measureType;
    @JsonProperty("sensorType")
    protected String sensorType;
    @JsonProperty("sampleRate")
    protected Integer sampleRate;

    public void fill(MeasurementReading data, SensorSingleReading reading, SensorRoot root, DateInfo date) {
        super.fill(data, reading, root, date);

        data.addressId = reading.getAddress();
        data.measureType = reading.getMeasureType();
        data.sensorType = reading.getSensorType();
        data.sampleRate = reading.getSampleRate();
    }

    @Override
    public String toString() {
        return "MeasurementReading{" +
                super.toString() +
                ", addressId=" + addressId +
                ", measureType='" + measureType + '\'' +
                ", sensorType='" + sensorType + '\'' +
                ", sampleRate=" + sampleRate +
                '}';
    }
}
