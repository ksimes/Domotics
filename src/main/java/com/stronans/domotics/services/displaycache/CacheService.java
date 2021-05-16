package com.stronans.domotics.services.displaycache;

import com.stronans.domotics.dao.CacheDAO;
import com.stronans.domotics.model.sensors.SensorCache;
import com.stronans.domotics.model.sensors.SensorMeasurement;
import com.stronans.domotics.model.sensors.SensorReadingSingleEntry;
import com.stronans.domotics.utilities.DateInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles the processing of temperature values sent for stations and requested by query service.
 * Created by S.King on 03/07/2016.
 */
@Service("CacheService")
@Slf4j
public class CacheService {
    private CacheDAO cacheDAO;

    @Autowired
    public CacheService(CacheDAO cacheDAO) {
        this.cacheDAO = cacheDAO;
    }

    public SensorMeasurement saveMeasurement(SensorMeasurement readingToCache, DateInfo current) {
        readingToCache = cacheDAO.update(readingToCache, current);
        return readingToCache;
    }

    public SensorMeasurement saveNewMeasurement(List<SensorReadingSingleEntry> collection) {
        for (SensorReadingSingleEntry reading : collection) {
            cacheDAO.updateNew(reading);
        }
        return null;
    }

    public List<SensorCache> findAll() {
        return cacheDAO.getList();
    }
}
