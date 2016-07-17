package com.stronans.domotics.services.measurement;

import com.stronans.domotics.dao.TemperatureDAO;
import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles the processing of temperature values sent for tations and requested by query service.
 * Created by S.King on 03/07/2016.
 */
@Service("TemperatureService")
public class TemperatureService implements MeasurementServiceInterface {
    private static final Logger logger = Logger.getLogger(TemperatureService.class);

    @Autowired
    private TemperatureDAO temperatureDAO;

    @Override
    public Measurement saveMeasurement(Measurement temperature) {
        logger.info("Save to DB - Station: " +
                temperature.stationId() + " temp: " +
                temperature.value() + " at " +
                temperature.timeStamp());

        temperature = temperatureDAO.add(temperature);
        return temperature;
    }

    @Override
    public List<Measurement> find() {
        return temperatureDAO.getList();
    }

    @Override
    public List<Measurement> find(long stationId) {
        return temperatureDAO.getList(stationId);
    }

    @Override
    public List<Measurement> find(long stationId, DateInfo startDate, DateInfo endDate) {
        return temperatureDAO.getList(stationId, startDate, endDate);
    }

    @Override
    public List<Measurement> find(DateInfo startDate, DateInfo endDate) {
        return temperatureDAO.getList(0, startDate, endDate);
    }

    @Override
    public Measurement findLatest(long stationId) {
        return temperatureDAO.getLatest(stationId);
    }

    @Override
    public List<Measurement> findAllToday(long stationId) {
        return temperatureDAO.getList(stationId, DateInfo.getNow().addToDate(-1), DateInfo.getUndefined());
    }
}
