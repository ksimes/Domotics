package com.stronans.domotics.controller;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.services.measurement.MeasurementServiceInterface;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by S.King on 11/07/2016.
 */
public class HeatIndexController {
    private static final Logger logger = Logger.getLogger(HeatIndexController.class);
    private static int ALL_STATIONS = 0;

    @Resource(name="HeatIndexService")
    MeasurementServiceInterface heatIndexService;      //Service which will do all data retrieval/manipulation work

    //------------------- Retrieve All HeatIndices in DB --------------------------------------------------------

    @RequestMapping(value = "/heatindex/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> listAllHeatIndexValues() {
        logger.info("Hit listAll");
        List<Measurement> measurements = heatIndexService.find();
        if (measurements.isEmpty()) {
            return new ResponseEntity<List<Measurement>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Measurement>>(measurements, HttpStatus.OK);
    }

    //------------------- Retrieve All HeatIndices for a given date --------------------------------------------------------

    @RequestMapping(value = "/heatindex/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getHeatIndexValuesByDate(@PathVariable("startDate") DateInfo startDate, @PathVariable("endDate") DateInfo endDate) {
        List<Measurement> measurements = heatIndexService.find(ALL_STATIONS, startDate, endDate);
        if (measurements.isEmpty()) {
            return new ResponseEntity<List<Measurement>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Measurement>>(measurements, HttpStatus.OK);
    }

    //------------------- Retrieve All HeatIndices for a given Station --------------------------------------------------------

    @RequestMapping(value = "/heatindex/{station}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Measurement>> getHeatIndexValuesByStation(@PathVariable("station") long stationId) {
        List<Measurement> measurements = heatIndexService.find(stationId);
        if (measurements.isEmpty()) {
            return new ResponseEntity<List<Measurement>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Measurement>>(measurements, HttpStatus.OK);
    }

}
