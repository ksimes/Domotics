package com.stronans.domotics.model.measurements.temperature;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stronans.domotics.model.measurements.MeasurementReading;
import com.stronans.domotics.model.sensors.SensorDetector;
import com.stronans.domotics.model.sensors.SensorProcessor;
import com.stronans.domotics.model.sensors.SensorRoot;
import com.stronans.domotics.model.sensors.SensorSingleReading;
import com.stronans.domotics.utilities.DateInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@Slf4j
public class MCP9808Measurement extends MeasurementReading implements SensorProcessor, SensorDetector {
    @JsonProperty("resolution")
    private int resolution;
    @JsonProperty("celsius")
    private double celsius;

    public boolean canTranslate(SensorSingleReading reading) {
        boolean result = false;
        if ("TEMPERATURE".equals(reading.getMeasureType().toUpperCase())) {
            if ("MCP9808".equals(reading.getSensorType().toUpperCase())) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public MCP9808Measurement translate(SensorSingleReading reading, SensorRoot root, DateInfo date) {
        MCP9808Measurement result = new MCP9808Measurement();
        super.fill(result, reading, root, date);

        Map<String, Object> node = reading.getPayload();

        if (node != null) {
            result.resolution = new Integer(node.get("resolution").toString());
            result.celsius = new Double(node.get("celsius").toString());
        }
        return result;
    }

    @Override
    public String toString() {
        return "MCP9808Measurement{" +
                super.toString() +
                ", resolution=" + resolution +
                ", celsius=" + celsius +
                '}';
    }
}
