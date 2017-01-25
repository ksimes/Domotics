package com.stronans.domotics.database;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.utilities.DateInfo;

import java.util.List;

/**
 * Created by S.King on 07/07/2016.
 */
public interface MeasurementConnectorInterface {

    Measurement add(Measurement measurement);

    List<Measurement> getList(long stationId, DateInfo startDate, DateInfo endDate, boolean latest);

    Long getCount(long stationId, DateInfo startDate, DateInfo endDate);
}
