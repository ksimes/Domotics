package com.stronans.domotics.controller;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.services.measurement.MeasurementServiceInterface;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Handles all temperature REST calls.
 * <p>
 * Created by S.King on 11/07/2016.
 */
@RestController
@RequestMapping(value = "/domotic/api/temperature")
public class TemperatureController {
    private static final Logger logger = Logger.getLogger(TemperatureController.class);
    private static int ALL_STATIONS = 0;

    @Resource(name = "TemperatureService")
    MeasurementServiceInterface temperatureService;  //Service which will do all data retrieval/manipulation work

    //------------------- Retrieve All Temperatures in DB --------------------------------------------------------
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> listAllTempValues() {
        logger.debug("Hit listAllTempValues");
        List<Measurement> measurements = temperatureService.find();
        if (measurements.isEmpty()) {
            return new ResponseEntity<List<Measurement>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("Access-Control-Allow-Origin", "http://localhost:3000");
        return new ResponseEntity<List<Measurement>>(measurements, responseHeaders, HttpStatus.OK);
    }

    //------------------- Retrieve All Temperatures for a given Station --------------------------------------------------------
    @RequestMapping(value = "/{station}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getTempValuesByStation(@PathVariable("station") long stationId) {
        logger.debug("Hit getTempValuesByStation");
        List<Measurement> measurements = temperatureService.find(stationId);
        if (measurements.isEmpty()) {
            return new ResponseEntity<List<Measurement>>(measurements, HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Measurement>>(measurements, HttpStatus.OK);
    }

    //------------------- Retrieve Temperatures for a given Station by date range --------------------------------------------------------
    @RequestMapping(value = "/{station}/range/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getTempValuesByRange(@PathVariable("station") long stationId,
                                                                  @PathVariable("startDate") String startDateString,
                                                                  @PathVariable("endDate") String endDateString) {
        DateInfo startDate = DateInfo.getUndefined();
        DateInfo endDate = DateInfo.getUndefined();

        logger.debug("StartDateString:" + startDateString);
        if (DateInfo.isUniversalString(startDateString)) {
            startDate = DateInfo.fromUniversalString(startDateString);
            logger.debug("Converted " + startDate);
        }

        logger.debug("EndDateString:" + endDateString);
        if (DateInfo.isUniversalString(endDateString)) {
            endDate = DateInfo.fromUniversalString(endDateString);
        }

        List<Measurement> measurements = temperatureService.find(stationId, startDate, endDate);
        if (measurements.isEmpty()) {
            return new ResponseEntity<List<Measurement>>(measurements, HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Measurement>>(measurements, HttpStatus.OK);
    }

    //------------------- Retrieve latest temperature for a given Station --------------------------------------------------------
    @RequestMapping(value = "/{station}/latest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Measurement> getTempValueLatest(@PathVariable("station") long stationId) {
        logger.debug("Hit getTempValueLatest");
        Measurement measurement = temperatureService.findLatest(stationId);

        if (measurement == null) {
            return new ResponseEntity<Measurement>(measurement, HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }

        return new ResponseEntity<Measurement>(measurement, HttpStatus.OK);
    }

    //------------------- Retrieve All Temperatures for a given date --------------------------------------------------------
//    @RequestMapping(value = "/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<Measurement>> getTempValuesByDate(@PathVariable("startDate") DateInfo startDate, @PathVariable("endDate") DateInfo endDate) {
//        logger.debug("Hit listByDate");
//        List<Measurement> measurements = temperatureService.find(ALL_STATIONS, startDate, endDate);
//        if (measurements.isEmpty()) {
//            return new ResponseEntity<List<Measurement>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
//        }
//        return new ResponseEntity<List<Measurement>>(measurements, HttpStatus.OK);
//    }
}
