package com.stronans.domotics.controller;

import com.stronans.domotics.model.SensorType;
import com.stronans.domotics.services.sensortype.SensorTypeService;
import org.apache.log4j.Logger;
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

    @Autowired
    private SensorTypeService sensorTypeService;  //Service which will do all data retrieval/manipulation work

    //------------------- Retrieve the details for a given SensorType --------------------------------------------------------
    @RequestMapping(value = "/{sensorTypeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SensorType> getSensorTypeName(@PathVariable("sensorTypeId") long sensorTypeId) {
        SensorType sensorType = sensorTypeService.find(sensorTypeId);
        if (sensorType == null) {
            return new ResponseEntity<>(sensorType, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE); // returns 416 or maybe "406 - Not Acceptable"
        } else {
            return new ResponseEntity<>(sensorType, HttpStatus.OK);
        }
    }

    //------------------- Retrieve the details for all SensorType --------------------------------------------------------
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SensorType>> getAllSensorTypes() {
        List<SensorType> sensorTypes = sensorTypeService.find();
        return new ResponseEntity<List<SensorType>>(sensorTypes, HttpStatus.OK);
    }
}
