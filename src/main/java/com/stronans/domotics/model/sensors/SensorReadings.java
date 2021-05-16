package com.stronans.domotics.model.sensors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class SensorReadings extends SensorRoot {
    @JsonProperty("readings")
    private List<SensorSingleReading> readings;
}
