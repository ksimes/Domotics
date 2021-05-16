package com.stronans.domotics.model.sensors;

import com.stronans.domotics.model.measurements.MeasurementReading;
import com.stronans.domotics.utilities.DateInfo;

public interface SensorDetector extends SensorProcessor {

    String description();

    boolean canTranslate(SensorSingleReading reading);

    @Override
    default MeasurementReading translate(SensorSingleReading reading, SensorRoot root, DateInfo date) {
        return null;
    }

    @Override
    default SensorReadingSingleEntry flatten(SensorSingleReading reading, SensorRoot root, DateInfo date) {
        return new SensorReadingSingleEntry(reading, root, date);
    }
}
