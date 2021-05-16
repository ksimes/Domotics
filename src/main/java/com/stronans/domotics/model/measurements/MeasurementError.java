package com.stronans.domotics.model.measurements;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MeasurementError extends MeasurementReading implements SensorProcessor, SensorDetector {
    @JsonProperty("message")
    private String message;
    @JsonProperty("details")
    private String details;

    @Override
    public String description() {
        return null;
    }

    @Override
    public boolean canTranslate(SensorSingleReading reading) {
        boolean result = false;
        if ("ERROR".equals(reading.getSensorType().toUpperCase())) {
            result = true;
        }
        return result;
    }

    @Override
    public MeasurementError translate(SensorSingleReading reading, SensorRoot root, DateInfo date) {
        MeasurementError result = new MeasurementError();
        super.fill(result, reading, root, date);

        Map<String, Object> node = reading.getPayload();

        if (node != null) {
            result.message = node.get("message").toString();
            result.details = node.get("details").toString();
        }

        return result;
    }
}
