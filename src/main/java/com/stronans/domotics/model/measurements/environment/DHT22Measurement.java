package com.stronans.domotics.model.measurements.environment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stronans.domotics.model.measurements.MeasurementReading;
import com.stronans.domotics.model.sensors.SensorDetector;
import com.stronans.domotics.model.sensors.SensorProcessor;
import com.stronans.domotics.model.sensors.SensorRoot;
import com.stronans.domotics.model.sensors.SensorSingleReading;
import com.stronans.domotics.utilities.DateInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
@Slf4j
public class DHT22Measurement extends MeasurementReading implements SensorProcessor, SensorDetector {
    @JsonProperty("resolution")
    private int resolution;
    @JsonProperty("celsius")
    private double celsius;
    @JsonProperty("humidity")
    private double humidity;
    @JsonProperty("humiture")
    private double humiture;

    @Override
    public boolean canTranslate(SensorSingleReading reading) {
        boolean result = false;
        if ("ENVIRONMENT".equals(reading.getMeasureType().toUpperCase())) {
            if ("DHT11".equals(reading.getSensorType().toUpperCase())) {
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
    public DHT22Measurement translate(SensorSingleReading reading, SensorRoot root, DateInfo date) {
        DHT22Measurement result = new DHT22Measurement();
        super.fill(result, reading, root, date);

        Map<String, Object> node = reading.getPayload();

        if (node != null) {
            result.resolution = new Integer(node.get("resolution").toString());
            result.celsius = new Double(node.get("celsius").toString());
            result.humidity = new Double(node.get("humidity").toString());
            result.humiture = new Double(node.get("humiture").toString());
        }

        return result;
    }

    @Override
    public String toString() {
        return "DHT22Measurement{" +
                super.toString() +
                ", resolution=" + resolution +
                ", celsius=" + celsius +
                ", humidity=" + humidity +
                ", humiture=" + humiture +
                '}';
    }
}
