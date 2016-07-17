package com.stronans.domotics.dao;

import com.stronans.domotics.database.StationConnector;
import com.stronans.domotics.model.Station;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by S.King on 14/07/2016.
 */
@Repository
public class StationDAO {
    private static final Logger logger = Logger.getLogger(StationDAO.class);

    private StationConnector connector = new StationConnector();

    public List<Station> getList() {
        return connector.getList(0);
    }

    public Station getStation(long stationId) {
        List<Station> list = connector.getList(stationId);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
