package com.stronans.domotics.controller;

import com.stronans.domotics.model.sensors.SensorCache;
import com.stronans.domotics.services.displaycache.CacheService;
import com.stronans.domotics.utilities.WebUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Handles all sensorCache REST calls.
 * <p>
 * Created by S.King on 22/07/2018.
 */
@RestController
@RequestMapping(value = "/domotic/api/sensorcache")
public class SensorCacheController {
//    private static final Logger logger = Logger.getLogger(StationController.class);

    private final CacheService cacheService;  //Service which will do all data retrieval/manipulation work

    @Autowired
    public SensorCacheController(final CacheService cacheService) {
        this.cacheService = cacheService;
    }

    //------------------- Retrieve the details for all Stations in the cache ----------------------------------------
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SensorCache>> getAllCacheData() {
        List<SensorCache> sensorCache = cacheService.findAll();
        return new ResponseEntity<>(sensorCache, WebUtilities.header(), HttpStatus.OK);
    }
}
