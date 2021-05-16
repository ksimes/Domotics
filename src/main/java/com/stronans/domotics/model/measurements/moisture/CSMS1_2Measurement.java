package com.stronans.domotics.model.measurements.moisture;

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
public class CSMS1_2Measurement extends MeasurementReading implements SensorProcessor, SensorDetector {
    @JsonProperty("moistureLevel")
    private int moistureLevel;

    public boolean canTranslate(SensorSingleReading reading) {
        boolean result = false;
        if ("MOISTURE".equals(reading.getMeasureType().toUpperCase())) {
            if ("CSM1.2".equals(reading.getSensorType().toUpperCase())) {
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
    public CSMS1_2Measurement translate(SensorSingleReading reading, SensorRoot root, DateInfo date) {
        CSMS1_2Measurement result = new CSMS1_2Measurement();
        super.fill(result, reading, root, date);

        Map<String, Object> node = reading.getPayload();
        if (node != null) {
            result.moistureLevel = new Integer(node.get("moistureLevel").toString());
        }
        return result;
    }

    @Override
    public String toString() {
        return "CSMS1_2Measurement{" +
                super.toString() +
                ", moistureLevel=" + moistureLevel +
                '}';
    }
}
