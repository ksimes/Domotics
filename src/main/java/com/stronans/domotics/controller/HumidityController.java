package com.stronans.domotics.controller;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.services.measurement.MeasurementServiceInterface;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Processes rest calls for humidity
 * <p>
 * Created by S.King on 11/07/2016.
 */
@RestController
@RequestMapping(value = "/domotic/api/humidity")
public class HumidityController extends MeasurementController {
    private static final Logger logger = Logger.getLogger(HumidityController.class);

    @Resource(name = "HumidityService")
    MeasurementServiceInterface humidityService;        //Service which will do all data retrieval/manipulation work

    //------------------- Retrieve All Humidities in DB --------------------------------------------------------
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> listAllHumidityValues() {
        return listAllValues(humidityService);
    }

    //------------------- Retrieve All Humidities for a given Station --------------------------------------------------------

    @RequestMapping(value = "/{station}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getHumidityValuesByStation(@PathVariable("station") long stationId) {
        return getValuesByStation(humidityService, stationId);
    }

    //------------------- Retrieve Humidities for a given Station by date range --------------------------------------------------------
    @RequestMapping(value = "/{station}/range/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getHumidityValuesByRange(@PathVariable("station") long stationId,
                                                                  @PathVariable("startDate") String startDateString,
                                                                  @PathVariable("endDate") String endDateString) {

        return getValuesByRange(humidityService, stationId, startDateString, endDateString);
    }

    //------------------- Retrieve Temperatures for a given Station for a given date --------------------------------------------------------
    @RequestMapping(value = "/{station}/date/{startDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getHumidityValuesByRange(@PathVariable("station") long stationId,
                                                                  @PathVariable("startDate") String startDateString) {

        return getValuesByRange(humidityService, stationId, startDateString, "0");
    }

    //------------------- Retrieve latest Humidity for a given Station --------------------------------------------------------
    @RequestMapping(value = "/{station}/latest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Measurement> getHumidityValueLatest(@PathVariable("station") long stationId) {
        return getTempValueLatest(humidityService, stationId);
    }
}
