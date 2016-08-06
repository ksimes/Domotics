package com.stronans.domotics.dao;

import com.stronans.domotics.database.MeasurementConnector;
import com.stronans.domotics.database.MeasurementConnectorInterface;
import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * Created by S.King on 06/07/2016.
 */
@Repository("TemperatureDAO")
public class TemperatureDAO implements MeasurementDAOInterface {
//    private static final Logger logger = Logger.getLogger(TemperatureDAO.class);

    private MeasurementConnectorInterface connector;

    public TemperatureDAO() {
        String TABLE_NAME = "temperature";
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
        return connector.getList(stationId, startDate, endDate, false);
    }

    @Override
    public Measurement getLatest(long stationId) {
        List<Measurement> list = connector.getList(stationId, DateInfo.getUndefined(), DateInfo.getUndefined(), true);
        if(list.isEmpty()) {
            return null;
        }
        else {
            return list.get(0);
        }
    }
}
