package com.stronans.domotics.services.measurement;

import com.stronans.domotics.dao.HumidityDAO;
import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles the processing of temperature values sent for stations and requested by query service.
 * Created by S.King on 03/07/2016.
 */
@Service("HumidityService")
public class HumidityService implements MeasurementServiceInterface {
    private static final Logger logger = Logger.getLogger(HumidityService.class);

    @Autowired
    private HumidityDAO humidityDAO;

    @Override
    public Measurement saveMeasurement(Measurement humidity) {
        logger.info("Save to DB - Station: " +
                humidity.stationId() + " humidity: " +
                humidity.value() + " at " +
                humidity.timeStamp());

        humidity = humidityDAO.add(humidity);
        return humidity;
    }

    @Override
    public List<Measurement> find() {
        return null;
    }

    @Override
    public List<Measurement> find(long stationId) {
        return null;
    }

    @Override
    public List<Measurement> find(long stationId, DateInfo startDate, DateInfo endDate) {
        return null;
    }

    @Override
    public List<Measurement> find(DateInfo startDate, DateInfo endDate) {
        return null;
    }

    @Override
    public Measurement findLatest(long stationId) {
        return null;
    }

    @Override
    public List<Measurement> findAllToday(long stationId) {
        return humidityDAO.getList(stationId, DateInfo.getNow().addToDate(-1), DateInfo.getUndefined());
    }
}
