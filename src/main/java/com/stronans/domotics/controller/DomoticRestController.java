package com.stronans.domotics.controller;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.model.SensorReading;
import com.stronans.domotics.services.measurement.MeasurementServiceInterface;
import com.stronans.domotics.utilities.DateInfo;
import com.stronans.domotics.utilities.WebUtilities;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    //------------------- Create a Measurement in DB --------------------------------------------------------
    @RequestMapping(value = "/reading/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Void> createMeasurement(@RequestBody SensorReading sensorReading) {
        DateInfo current = DateInfo.getNow();
        logger.info("Received from Station: " + sensorReading.getStationId()
                + " Temperature: " + sensorReading.getValue1()
                + " Humidity: " + sensorReading.getValue2()
                + " Heat Index: " + sensorReading.getValue3()
                + " Sample Rate: " + sensorReading.getSampleRate()
                + " Sensor Type: " + sensorReading.getSensorType()
                + " at " + current);

        temperatureService.saveMeasurement(new Measurement(sensorReading.getStationId(), sensorReading.getValue1(), current,
                sensorReading.getSampleRate(), sensorReading.getSensorType()));
        humidityService.saveMeasurement(new Measurement(sensorReading.getStationId(), sensorReading.getValue2(), current,
                sensorReading.getSampleRate(), sensorReading.getSensorType()));
        heatIndexService.saveMeasurement(new Measurement(sensorReading.getStationId(), sensorReading.getValue3(), current,
                sensorReading.getSampleRate(), sensorReading.getSensorType()));

        return new ResponseEntity<>(WebUtilities.header(), HttpStatus.CREATED);
    }
}