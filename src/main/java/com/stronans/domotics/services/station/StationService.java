package com.stronans.domotics.services.station;

import com.stronans.domotics.dao.StationDAO;
import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.model.Station;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by S.King on 14/07/2016.
 */
@Service
public class StationService {
    private static final Logger logger = Logger.getLogger(StationService.class);

    @Autowired
    private StationDAO stationDAO;

    public List<Station> find() {
        return stationDAO.getList();
    }

    public Station find(long stationId) {
        return stationDAO.getStation(stationId);
    }
}
