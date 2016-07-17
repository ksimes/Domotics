package com.stronans.domotics.dao;

import com.stronans.domotics.database.MeasurementConnector;
import com.stronans.domotics.database.MeasurementConnectorInterface;
import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.utilities.DateInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by S.King on 09/07/2016.
 */
@Repository("HumidityDAO")
public class HumidityDAO implements MeasurementDAOInterface {
    private MeasurementConnectorInterface connector;

    public HumidityDAO() {
        String TABLE_NAME = "humidity";
        connector = MeasurementConnector.create(TABLE_NAME);
    }

    @Override
    public Measurement add(Measurement measurement) {
        return connector.add(measurement);
    }

    @Override
    public List<Measurement> getList() {
        return connector.getList(0, DateInfo.getUndefined(), DateInfo.getUndefined(), false);
    }

    @Override
    public List<Measurement> getList(long stationId) {
        return connector.getList(stationId, DateInfo.getUndefined(), DateInfo.getUndefined(), false);
    }

    @Override
    public List<Measurement> getList(long stationId, DateInfo startDate, DateInfo endDate) {
        return null;
    }

    @Override
    public Measurement getLatest(long stationId) {
        return null;
    }
}
