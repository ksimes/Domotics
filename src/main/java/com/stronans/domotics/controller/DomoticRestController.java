package com.stronans.domotics.controller;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.model.Reading;
import com.stronans.domotics.model.Station;
import com.stronans.domotics.services.measurement.MeasurementServiceInterface;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/domotic/api/")
public class DomoticRestController {
    private static final Logger logger = Logger.getLogger(DomoticRestController.class);

    @Resource(name="TemperatureService")
    MeasurementServiceInterface temperatureService;  //Service which will do all data retrieval/manipulation work
    @Resource(name="HumidityService")
    MeasurementServiceInterface humidityService;        //Service which will do all data retrieval/manipulation work
    @Resource(name="HeatIndexService")
    MeasurementServiceInterface heatIndexService;      //Service which will do all data retrieval/manipulation work

    //------------------- Create a Measurement in DB --------------------------------------------------------
    @RequestMapping(value = "/reading/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Void> createMeasurement(@RequestBody Reading reading, UriComponentsBuilder ucBuilder) {
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
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
}