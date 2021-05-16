package com.stronans.domotics.model.sensors;

import com.stronans.domotics.model.measurements.MeasurementReading;
import com.stronans.domotics.utilities.DateInfo;

public interface SensorProcessor {
    MeasurementReading translate(SensorSingleReading reading, SensorRoot root, DateInfo date);

    SensorReadingSingleEntry flatten(SensorSingleReading reading, SensorRoot root, DateInfo date);
}
