package com.stronans.domotics.dao;

import com.stronans.domotics.database.DBConnection;
import com.stronans.domotics.database.MeasurementConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by S.King on 09/07/2016.
 * Extensively restructured by S.King on 11/04/2018.
 */
@Repository("HumidityDAO")
public class HumidityDAO extends MeasurementConnector {
    @Autowired
    public HumidityDAO(DBConnection dbConnection) {
        super(dbConnection, "humidity");
    }
}
