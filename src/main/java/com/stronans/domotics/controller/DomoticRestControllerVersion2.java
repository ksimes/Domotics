package com.stronans.domotics.controller;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.model.measurements.MeasurementReading;
import com.stronans.domotics.model.sensors.*;
import com.stronans.domotics.services.displaycache.CacheService;
import com.stronans.domotics.services.measurement.MeasurementServiceInterface;
import com.stronans.domotics.utilities.DateInfo;
import com.stronans.domotics.utilities.WebUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles input of JSON values coming from multiple measuring stations
 * which take in temperature, humidity, Heat Index and sample rate (usually DHT22 sensors).
 * <p>
 * Created by S.King on 11/07/2016.
 * Updated by S.King on 18/12/2020.
 */
@Slf4j
@RestController
@RequestMapping(value = "/domotic/2.0/api/")
public class DomoticRestControllerVersion2 {

    @Resource(name = "TemperatureService")
    private MeasurementServiceInterface temperatureService; //Service which will do all data retrieval/manipulation work
    @Resource(name = "HumidityService")
    private MeasurementServiceInterface humidityService;    // Service which will do all data retrieval/manipulation work
    @Resource(name = "HeatIndexService")
    private MeasurementServiceInterface heatIndexService;   //Service which will do all data retrieval/manipulation work
    @Resource
    private CacheService cacheService;

    private byte[] cachedBody;

    private final List<SensorDetector> sensorTypes;

    public DomoticRestControllerVersion2(List<SensorDetector> sensorTypes) {
        this.sensorTypes = sensorTypes;
    }

    //------------------- Create a Measurement in DB --------------------------------------------------------
    @RequestMapping(value = "/reading/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Void> createMeasurement(HttpServletRequest request, @RequestBody SensorReadings incomingSensorMeasurement) {
        DateInfo current = DateInfo.getNow();

        log.info("Received request {} from Cluster: {}", request.getRequestURL(), incomingSensorMeasurement.getCluster());

        processMeasurements(incomingSensorMeasurement, current);

        return new ResponseEntity<>(WebUtilities.header(), HttpStatus.CREATED);
    }

    private void processMeasurements(SensorReadings measurement, DateInfo current) {
        SensorMeasurement sensorMeasurement = null;

        List<MeasurementReading> collection = new ArrayList<>();
        List<SensorReadingSingleEntry> cacheCollection = new ArrayList<>();

        // Process data for this version of the protocol
        if (measurement.getSoftware() != null && measurement.getSoftware().equals("2.0")) {
            for (SensorSingleReading measure : measurement.getReadings()) {
                for (SensorDetector sensor : sensorTypes) {
                    if (sensor.canTranslate(measure)) {
                        MeasurementReading mr = sensor.translate(measure, measurement, current);
                        collection.add(mr);
                        log.info("Measurement {}", mr);
                        SensorReadingSingleEntry srre = sensor.flatten(measure, measurement, current);
                        cacheCollection.add(srre);
                        log.info("single reading {}", srre);
                        break;
                    }
                }
            }
        }

        cacheService.saveNewMeasurement(cacheCollection);

//        temperatureService.saveMeasurement(new Measurement(measurement.getClusterId(), measurement.getAddress(), ((DHT22Measurement) measurement).getCelsius(), current,
//                measurement.getSampleRate(), measurement.getSensorType()));
//        humidityService.saveMeasurement(new Measurement(measurement.getClusterId(), measurement.getAddress(), ((DHT22Measurement) measurement).getHumidity(), current,
//                measurement.getSampleRate(), measurement.getSensorType()));
//        heatIndexService.saveMeasurement(new Measurement(measurement.getClusterId(), measurement.getAddress(), ((DHT22Measurement) measurement).getHumiture(), current,
//                measurement.getSampleRate(), measurement.getSensorType()));
    }

    private void saveMeasurement(SensorMeasurement sensorMeasurement, DateInfo current) {
        log.info(" Temperature: " + sensorMeasurement.getValue1()
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
}
