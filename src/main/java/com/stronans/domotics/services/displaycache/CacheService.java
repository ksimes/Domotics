package com.stronans.domotics.services.displaycache;

import com.stronans.domotics.dao.CacheDAO;
import com.stronans.domotics.model.SensorCache;
import com.stronans.domotics.model.SensorMeasurement;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles the processing of temperature values sent for stations and requested by query service.
 * Created by S.King on 03/07/2016.
 */
@Service("CacheService")
public class CacheService {
    private static final Logger logger = Logger.getLogger(CacheService.class);

    private CacheDAO cacheDAO;

    @Autowired
    public CacheService(CacheDAO cacheDAO) {
        this.cacheDAO = cacheDAO;
    }

    public SensorMeasurement saveMeasurement(SensorMeasurement readingToCache, DateInfo current) {
        readingToCache = cacheDAO.update(readingToCache, current);
        return readingToCache;
    }

    public List<SensorCache> findAll() {
        return cacheDAO.getList();
    }
}
