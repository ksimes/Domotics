package com.stronans.domotics.services.measurement;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.utilities.DateInfo;

import java.util.List;

/**
 * Interface used by all services which send measurements to the Database and pull them back out.
 *
 * Created by S.King on 03/07/2016.
 */
public interface MeasurementServiceInterface {
    List<Measurement> find();                   // Find all temperature values
    List<Measurement> find(long stationId);     // Find all temperature values by station
    List<Measurement> find(long stationId, DateInfo startDate, DateInfo endDate);
    List<Measurement> find(DateInfo startDate, DateInfo endDate);
    Measurement findLatest(long stationId);
    Measurement saveMeasurement(Measurement t);
}
