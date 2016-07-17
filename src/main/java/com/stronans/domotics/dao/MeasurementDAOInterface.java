package com.stronans.domotics.dao;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.utilities.DateInfo;

import java.util.List;

/**
 * Created by S.King on 06/07/2016.
 */
public interface MeasurementDAOInterface {

    Measurement add(Measurement measurement);
    List<Measurement> getList();
    List<Measurement> getList(long stationId);
    List<Measurement> getList(long stationId, DateInfo startDate, DateInfo endDate);
    Measurement getLatest(long stationId);
}
