package com.stronans.domotics.controller;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.model.Reading;
import com.stronans.domotics.services.measurement.MeasurementServiceInterface;
import com.stronans.domotics.utilities.DateInfo;
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
 * which take in temperture, humidity and Heat Index (usually DHT22 sensors).
 * <p>
 * Created by S.King on 11/07/2016.
 */
@RestController
@RequestMapping(value = "/domotic/api/")
public class DomoticRestController {
    private static final Logger logger = Logger.getLogger(DomoticRestController.class);

    @Resource(name = "TemperatureService")
    MeasurementServiceInterface temperatureService;  //Service which will do all data retrieval/manipulation work
    @Resource(name = "HumidityService")
    MeasurementServiceInterface humidityService;        //Service which will do all data retrieval/manipulation work
    @Resource(name = "HeatIndexService")
    MeasurementServiceInterface heatIndexService;      //Service which will do all data retrieval/manipulation work

    //------------------- Create a Measurement in DB --------------------------------------------------------
    @RequestMapping(value = "/reading/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Void> createMeasurement(@RequestBody Reading reading) {
        DateInfo current = DateInfo.getNow();
        logger.info("Received from Station: " + reading.getStationId()
                + " Temperature: " + reading.getTemperatureValue()
                + " Humidity: " + reading.getHumidityValue()
                + " Heat Index: " + reading.getHumitureValue()
                + " at " + current);

        temperatureService.saveMeasurement(new Measurement(reading.getStationId(), reading.getTemperatureValue(), current));
        humidityService.saveMeasurement(new Measurement(reading.getStationId(), reading.getHumidityValue(), current));
        heatIndexService.saveMeasurement(new Measurement(reading.getStationId(), reading.getHumitureValue(), current));

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}