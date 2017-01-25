package com.stronans.domotics.controller;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.services.measurement.MeasurementServiceInterface;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Abstract class to handle all REST calls
 * Created by S.King on 17/07/2016.
 */
abstract class MeasurementController {
    private static final Logger logger = Logger.getLogger(MeasurementController.class);

    ResponseEntity<List<Measurement>> listAllValues(MeasurementServiceInterface service) {
        List<Measurement> measurements = service.find();
        if (measurements.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);        //You many decide to return HttpStatus.NOT_FOUND
        }
        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("Access-Control-Allow-Origin", "http://localhost:3000");
        return new ResponseEntity<>(measurements, responseHeaders, HttpStatus.OK);
    }

    ResponseEntity<Long> countAllValues(MeasurementServiceInterface service) {
        Long measurementCount = service.count();
        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("Access-Control-Allow-Origin", "http://localhost:3000");
        return new ResponseEntity<>(measurementCount, responseHeaders, HttpStatus.OK);
    }

    ResponseEntity<List<Measurement>> getValuesByStation(MeasurementServiceInterface service, long stationId) {
        List<Measurement> measurements = service.find(stationId);
        if (measurements.isEmpty()) {
            return new ResponseEntity<>(measurements, HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }

    ResponseEntity<Long> getCountByStation(MeasurementServiceInterface service, long stationId) {
        Long measurementCount = service.count(stationId);
        return new ResponseEntity<>(measurementCount, HttpStatus.OK);
    }

    ResponseEntity<List<Measurement>> getValuesByRange(MeasurementServiceInterface service, long stationId,
                                                       String startDateString,
                                                       String endDateString) {
        DateInfo startDate = DateInfo.getUndefined();
        DateInfo endDate = DateInfo.getUndefined();

        logger.trace("StartDateString:" + startDateString);
        if (DateInfo.isUniversalString(startDateString)) {
            startDate = DateInfo.fromUniversalString(startDateString);
            logger.trace("StartDate Converted " + startDate);
        }

        logger.trace("EndDateString:" + endDateString);
        if (DateInfo.isUniversalString(endDateString)) {
            endDate = DateInfo.fromUniversalString(endDateString);
            logger.trace("EndDate Converted " + endDate);
        }

        List<Measurement> measurements = service.find(stationId, startDate, endDate);
        if (measurements.isEmpty()) {
            return new ResponseEntity<>(measurements, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE); // returns 416 or maybe "406 - Not Acceptable"
        }
        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }

    ResponseEntity<Long> getCountByRange(MeasurementServiceInterface service, long stationId,
                                                       String startDateString,
                                                       String endDateString) {
        DateInfo startDate = DateInfo.getUndefined();
        DateInfo endDate = DateInfo.getUndefined();

        logger.trace("StartDateString:" + startDateString);
        if (DateInfo.isUniversalString(startDateString)) {
            startDate = DateInfo.fromUniversalString(startDateString);
            logger.trace("StartDate Converted " + startDate);
        }

        logger.trace("EndDateString:" + endDateString);
        if (DateInfo.isUniversalString(endDateString)) {
            endDate = DateInfo.fromUniversalString(endDateString);
            logger.trace("EndDate Converted " + endDate);
        }

        Long measurementCount = service.count(stationId, startDate, endDate);
        return new ResponseEntity<>(measurementCount, HttpStatus.OK);
    }

    ResponseEntity<Measurement> getTempValueLatest(MeasurementServiceInterface service, long stationId) {

        Measurement measurement = service.findLatest(stationId);

        if (measurement == null) {
            return new ResponseEntity<>(measurement, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        }

        return new ResponseEntity<>(measurement, HttpStatus.OK);
    }
}
