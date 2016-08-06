package com.stronans.domotics.services.sensortype;

import com.stronans.domotics.dao.SensorTypeDAO;
import com.stronans.domotics.model.SensorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to get back information about all of the sensor types available to deploy.
 * Created by S.King on 14/07/2016.
 */
@Service
public class SensorTypeService {
//    private static final Logger logger = Logger.getLogger(StationService.class);

    @Autowired
    private SensorTypeDAO sensorTypeDAO;

    public List<SensorType> find() {
        return sensorTypeDAO.getList();
    }

    public SensorType find(long sensorTypeId) {
        return sensorTypeDAO.getSensorType(sensorTypeId);
    }
}
