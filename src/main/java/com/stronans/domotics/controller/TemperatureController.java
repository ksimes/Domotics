package com.stronans.domotics.controller;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.services.measurement.MeasurementServiceInterface;
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
public class TemperatureController extends MeasurementController {
//    private static final Logger logger = Logger.getLogger(TemperatureController.class);

    @Resource(name = "TemperatureService")
    private MeasurementServiceInterface temperatureService;  //Service which will do all data retrieval/manipulation work

    //------------------- Retrieve All Temperatures in DB --------------------------------------------------------
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> listAllTempValues() {
        return listAllValues(temperatureService);
    }

    //------------------- Retrieve All Temperatures for a given Station --------------------------------------------------------
    @RequestMapping(value = "/{station}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getTempValuesByStation(@PathVariable("station") String stationId) {
        return getValuesByStation(temperatureService, stationId);
    }

    //------------------- Retrieve Temperatures for a given Station by date range --------------------------------------------------------
    @RequestMapping(value = "/{station}/range/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getTempValuesByRange(@PathVariable("station") String stationId,
                                                                  @PathVariable("startDate") String startDateString,
                                                                  @PathVariable("endDate") String endDateString) {

        return getValuesByRange(temperatureService, stationId, startDateString, endDateString);
    }

    //------------------- Retrieve Temperatures for a given Station for a given date --------------------------------------------------------
    @RequestMapping(value = "/{station}/date/{startDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getTempValuesByRange(@PathVariable("station") String stationId,
                                                                  @PathVariable("startDate") String startDateString) {

        return getValuesByRange(temperatureService, stationId, startDateString, "0");
    }

    //------------------- Retrieve latest temperature for a given Station --------------------------------------------------------
    @RequestMapping(value = "/{station}/latest/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Measurement> getTempValueLatest(@PathVariable("station") String stationId) {
        return getTempValueLatest(temperatureService, stationId);
    }

    //------------------- Retrieve Count of All Temperatures in DB --------------------------------------------------------
    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> countAllTempValues() {
        return countAllValues(temperatureService);
    }

    //------------------- Retrieve count of all Temperatures for a given Station --------------------------------------------------------
    @RequestMapping(value = "/{station}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> countTempValuesByStation(@PathVariable("station") String stationId) {
        return getCountByStation(temperatureService, stationId);
    }

    //------------------- Retrieve count of Temperatures for a given Station by date range --------------------------------------------------------
    @RequestMapping(value = "/{station}/count/range/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> countTempValuesByRange(@PathVariable("station") String stationId,
                                                       @PathVariable("startDate") String startDateString,
                                                       @PathVariable("endDate") String endDateString) {

        return getCountByRange(temperatureService, stationId, startDateString, endDateString);
    }

    //------------------- Retrieve Temperatures for a given Station for a given date --------------------------------------------------------
    @RequestMapping(value = "/{station}/count/date/{startDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> countTempValuesByRange(@PathVariable("station") String stationId,
                                                       @PathVariable("startDate") String startDateString) {

        return getCountByRange(temperatureService, stationId, startDateString, "0");
    }
}
