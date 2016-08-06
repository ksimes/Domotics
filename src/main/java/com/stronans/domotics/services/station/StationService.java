package com.stronans.domotics.services.station;

import com.stronans.domotics.dao.StationDAO;
import com.stronans.domotics.model.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to get back information about all of the stations setup throughout the house
 * Created by S.King on 14/07/2016.
 */
@Service
public class StationService {
//    private static final Logger logger = Logger.getLogger(StationService.class);

    @Autowired
    private StationDAO stationDAO;

    public List<Station> find() {
        return stationDAO.getList();
    }

    public Station find(long stationId) {
        return stationDAO.getStation(stationId);
    }
}
