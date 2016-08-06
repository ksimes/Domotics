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
 * Handles processing of all Heat Index values from the DB.
 * <p>
 * Created by S.King on 11/07/2016.
 */
@RestController
@RequestMapping(value = "/domotic/api/heatindex")
public class HeatIndexController extends MeasurementController {
    private static final Logger logger = Logger.getLogger(HeatIndexController.class);

    @Resource(name = "HeatIndexService")
    MeasurementServiceInterface heatIndexService;      //Service which will do all data retrieval/manipulation work

    //------------------- Retrieve All HeatIndices in DB --------------------------------------------------------
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> listAllHeatIndexValues() {
        return listAllValues(heatIndexService);
    }

    //------------------- Retrieve All HeatIndices for a given Station --------------------------------------------------------
    @RequestMapping(value = "/{station}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getHeatIndexValuesByStation(@PathVariable("station") long stationId) {
        return getValuesByStation(heatIndexService, stationId);
    }

    //------------------- Retrieve HeatIndices for a given Station by date range --------------------------------------------------------
    @RequestMapping(value = "/{station}/range/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getHeatIndexValuesByRange(@PathVariable("station") long stationId,
                                                                       @PathVariable("startDate") String startDateString,
                                                                       @PathVariable("endDate") String endDateString) {

        return getValuesByRange(heatIndexService, stationId, startDateString, endDateString);
    }

    //------------------- Retrieve HeatIndices for a given Station for a given date --------------------------------------------------------
    @RequestMapping(value = "/{station}/date/{startDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getHeatIndexValuesByRange(@PathVariable("station") long stationId,
                                                                       @PathVariable("startDate") String startDateString) {

        return getValuesByRange(heatIndexService, stationId, startDateString, "0");
    }

    //------------------- Retrieve latest HeatIndex for a given Station --------------------------------------------------------
    @RequestMapping(value = "/{station}/latest/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Measurement> getHeatIndexValueLatest(@PathVariable("station") long stationId) {
        return getTempValueLatest(heatIndexService, stationId);
    }
}
