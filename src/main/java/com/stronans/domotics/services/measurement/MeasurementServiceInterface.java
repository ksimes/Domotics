package com.stronans.domotics.services.measurement;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.utilities.DateInfo;

import java.util.List;

/**
 * Interface used by all services which send measurements to the Database and pull them back out.
 * <p>
 * Created by S.King on 03/07/2016.
 */
public interface MeasurementServiceInterface {

    // Saves off a value coming in from an environment station
    Measurement saveMeasurement(Measurement t);

    // Find all values in DB
    List<Measurement> find();

    // Find all values by station
    List<Measurement> find(String stationId);

    // Find all values by station, start date stamp and end datestamp
    List<Measurement> find(String stationId, DateInfo startDate, DateInfo endDate);

    // Find all values in DB by start date stamp and end datestamp
    List<Measurement> find(DateInfo startDate, DateInfo endDate);

    // Find the latest value in DB by station
    Measurement findLatest(String stationId);

    // Count all values in DB
    Long count();

    // Count all values in DB by station
    Long count(String stationId);

    // Count all values by station, start date stamp and end datestamp
    Long count(String stationId, DateInfo startDate, DateInfo endDate);

    // Count all values by start date stamp and end datestamp in DB
    Long count(DateInfo startDate, DateInfo endDate);
}
