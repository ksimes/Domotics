package com.stronans.domotics.controller;

import com.stronans.domotics.model.sensors.SensorType;
import com.stronans.domotics.services.sensortype.SensorTypeService;
import com.stronans.domotics.utilities.WebUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Handles all sensorType REST calls.
 * <p>
 * Created by S.King on 16/08/2016.
 */
@RestController
@RequestMapping(value = "/domotic/api/sensortype")
public class SensorTypeController {
//    private static final Logger logger = Logger.getLogger(SensorTypeController.class);

    private final SensorTypeService sensorTypeService;  //Service which will do all data retrieval/manipulation work

    @Autowired
    public SensorTypeController(final SensorTypeService sensorTypeService) {
        this.sensorTypeService = sensorTypeService;
    }

    //------------------- Retrieve the details for a given SensorType --------------------------------------------------------
    @RequestMapping(value = "/{sensorTypeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SensorType> getSensorTypeName(@PathVariable("sensorTypeId") long sensorTypeId) {
        SensorType sensorType = sensorTypeService.find(sensorTypeId);
        if (sensorType == null) {
            return new ResponseEntity<>(null, WebUtilities.header(), HttpStatus.NO_CONTENT); // returns 416 or maybe "406 - Not Acceptable"
        } else {
            return new ResponseEntity<>(sensorType, WebUtilities.header(), HttpStatus.OK);
        }
    }

    //------------------- Retrieve the details for all SensorType --------------------------------------------------------
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SensorType>> getAllSensorTypes() {
        List<SensorType> sensorTypes = sensorTypeService.find();
        return new ResponseEntity<>(sensorTypes, WebUtilities.header(), HttpStatus.OK);
    }
}
