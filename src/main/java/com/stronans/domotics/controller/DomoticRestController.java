package com.stronans.domotics.controller;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.model.sensors.SensorMeasurement;
import com.stronans.domotics.services.displaycache.CacheService;
import com.stronans.domotics.services.measurement.MeasurementServiceInterface;
import com.stronans.domotics.utilities.DateInfo;
import com.stronans.domotics.utilities.WebUtilities;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Handles input of JSON values coming from multiple measuring stations
 * which take in temperature, humidity, Heat Index and sample rate (usually DHT22 sensors).
 * <p>
 * Created by S.King on 11/07/2016.
 */
@RestController
@RequestMapping(value = "/domotic/api/")
public class DomoticRestController {
    private static final Logger logger = Logger.getLogger(DomoticRestController.class);

    @Resource(name = "TemperatureService")
    private MeasurementServiceInterface temperatureService;  //Service which will do all data retrieval/manipulation work
    @Resource(name = "HumidityService")
    private MeasurementServiceInterface humidityService;        //Service which will do all data retrieval/manipulation work
    @Resource(name = "HeatIndexService")
    private MeasurementServiceInterface heatIndexService;      //Service which will do all data retrieval/manipulation work
    @Resource
    private CacheService cacheService;

    private void saveMeasurement(SensorMeasurement sensorMeasurement, DateInfo current) {
        logger.info(" Temperature: " + sensorMeasurement.getValue1()
                + " Humidity: " + sensorMeasurement.getValue2()
                + " Heat Index: " + sensorMeasurement.getValue3()
                + " Sample Rate: " + sensorMeasurement.getSampleRate()
                + " Sensor Type: " + sensorMeasurement.getSensorType()
                + " at " + current);

        cacheService.saveMeasurement(sensorMeasurement, current);

        temperatureService.saveMeasurement(new Measurement(sensorMeasurement.getStationId(), sensorMeasurement.getValue1(), current,
                sensorMeasurement.getSampleRate(), sensorMeasurement.getSensorType()));
        humidityService.saveMeasurement(new Measurement(sensorMeasurement.getStationId(), sensorMeasurement.getValue2(), current,
                sensorMeasurement.getSampleRate(), sensorMeasurement.getSensorType()));
        heatIndexService.saveMeasurement(new Measurement(sensorMeasurement.getStationId(), sensorMeasurement.getValue3(), current,
                sensorMeasurement.getSampleRate(), sensorMeasurement.getSensorType()));
    }

    //------------------- Create a Measurement in DB --------------------------------------------------------
    @RequestMapping(value = "/reading/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Void> createMeasurement(@RequestBody SensorMeasurement sensorMeasurement) {
        DateInfo current = DateInfo.getNow();

        logger.info("Received from Station: " + sensorMeasurement.getStationId());

        saveMeasurement(sensorMeasurement, current);

        return new ResponseEntity<>(WebUtilities.header(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/readings/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Void> createMeasurements(@RequestBody List<SensorMeasurement> sensorMeasurements) {
        DateInfo current = DateInfo.getNow();

        logger.info("Received " + sensorMeasurements.size() + " readings from Station: " + sensorMeasurements.get(0).getStationId());

        for (SensorMeasurement sensorMeasurement : sensorMeasurements) {
            saveMeasurement(sensorMeasurement, current);
        }

        return new ResponseEntity<>(WebUtilities.header(), HttpStatus.CREATED);
    }
}
