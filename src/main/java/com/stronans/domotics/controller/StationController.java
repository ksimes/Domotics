package com.stronans.domotics.controller;

import com.stronans.domotics.model.Station;
import com.stronans.domotics.services.station.StationService;
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
 * Handles all station REST calls.
 * <p>
 * Created by S.King on 15/07/2016.
 */
@RestController
@RequestMapping(value = "/domotic/api/station")
public class StationController {
//    private static final Logger logger = Logger.getLogger(StationController.class);

    private final StationService stationService;  //Service which will do all data retrieval/manipulation work

    @Autowired
    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    //------------------- Retrieve the details for a given Station --------------------------------------------------------
    @RequestMapping(value = "/{stationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Station> getStationName(@PathVariable("stationId") long stationId) {
        Station station = stationService.find(stationId);
        if (station == null) {
            return new ResponseEntity<>(null, WebUtilities.header(), HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE); // returns 416 or maybe "406 - Not Acceptable"
        } else {
            return new ResponseEntity<>(station, WebUtilities.header(), HttpStatus.OK);
        }
    }

    //------------------- Retrieve the details for all Stations --------------------------------------------------------
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Station>> getAllStations() {
        List<Station> stations = stationService.find();
        return new ResponseEntity<>(stations, WebUtilities.header(), HttpStatus.OK);
    }
}
