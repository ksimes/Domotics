package com.stronans.domotics.dao;

import com.stronans.domotics.database.SensorTypeConnector;
import com.stronans.domotics.model.SensorType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Handles the connection to the SensorType connector to draw back SensorType data from the DB.
 * Created by S.King on 06/08/2016.
 */
@Repository
public class SensorTypeDAO {
//    private static final Logger logger = Logger.getLogger(StationDAO.class);

    private SensorTypeConnector connector = new SensorTypeConnector();

    public List<SensorType> getList() {
        return connector.getList(0);
    }

    public SensorType getSensorType(long stationId) {
        List<SensorType> list = connector.getList(stationId);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
