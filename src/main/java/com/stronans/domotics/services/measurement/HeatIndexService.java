package com.stronans.domotics.services.measurement;

import com.stronans.domotics.dao.HeatIndexDAO;
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
@Service("HeatIndexService")
public class HeatIndexService implements MeasurementServiceInterface {
    private static final Logger logger = Logger.getLogger(HeatIndexService.class);

    private HeatIndexDAO heatIndexDAO;

    @Autowired
    public HeatIndexService(HeatIndexDAO heatIndexDAO) {
        this.heatIndexDAO = heatIndexDAO;
    }

    @Override
    public Measurement saveMeasurement(Measurement heatIndex) {
        logger.debug("Save to DB - Station: " +
                heatIndex.stationId() + " HeatIndex: " +
                heatIndex.value() + " at " +
                heatIndex.timeStamp());

        heatIndex = heatIndexDAO.add(heatIndex);
        return heatIndex;
    }

    @Override
    public List<Measurement> find() {
        return heatIndexDAO.getList();
    }

    @Override
    public List<Measurement> find(String stationId) {
        return heatIndexDAO.getList(stationId);
    }

    @Override
    public List<Measurement> find(String stationId, DateInfo startDate, DateInfo endDate) {
        return heatIndexDAO.getList(stationId, startDate, endDate);
    }

    @Override
    public List<Measurement> find(DateInfo startDate, DateInfo endDate) {
        return heatIndexDAO.getList(null, startDate, endDate);
    }

    @Override
    public Measurement findLatest(String stationId) {
        return heatIndexDAO.getLatest(stationId);
    }

    @Override
    public Long count() {
        return heatIndexDAO.getItemCount();
    }

    @Override
    public Long count(String stationId) {
        return heatIndexDAO.getItemCount(stationId);
    }

    @Override
    public Long count(String stationId, DateInfo startDate, DateInfo endDate) {
        return heatIndexDAO.getItemCount(stationId, startDate, endDate);
    }

    @Override
    public Long count(DateInfo startDate, DateInfo endDate) {
        return heatIndexDAO.getItemCount(null, startDate, endDate);
    }
}
