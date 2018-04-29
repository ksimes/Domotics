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

    private HumidityDAO humidityDAO;

    @Autowired
    public HumidityService(HumidityDAO humidityDAO) {
        this.humidityDAO = humidityDAO;
    }

    @Override
    public Measurement saveMeasurement(Measurement humidity) {
        logger.debug("Save to DB - Station: " +
                humidity.stationId() + " humidity: " +
                humidity.value() + " at " +
                humidity.timeStamp());

        humidity = humidityDAO.add(humidity);
        return humidity;
    }

    @Override
    public List<Measurement> find() {
        return humidityDAO.getList();
    }

    @Override
    public List<Measurement> find(String stationId) {
        return humidityDAO.getList(stationId);
    }

    @Override
    public List<Measurement> find(String stationId, DateInfo startDate, DateInfo endDate) {
        return humidityDAO.getList(stationId, startDate, endDate);
    }

    @Override
    public List<Measurement> find(DateInfo startDate, DateInfo endDate) {
        return humidityDAO.getList(null, startDate, endDate);
    }

    @Override
    public Measurement findLatest(String stationId) {
        return humidityDAO.getLatest(stationId);
    }

    @Override
    public Long count() {
        return humidityDAO.getItemCount();
    }

    @Override
    public Long count(String stationId) {
        return humidityDAO.getItemCount(stationId);
    }

    @Override
    public Long count(String stationId, DateInfo startDate, DateInfo endDate) {
        return humidityDAO.getItemCount(stationId, startDate, endDate);
    }

    @Override
    public Long count(DateInfo startDate, DateInfo endDate) {
        return humidityDAO.getItemCount(null, startDate, endDate);
    }
}
