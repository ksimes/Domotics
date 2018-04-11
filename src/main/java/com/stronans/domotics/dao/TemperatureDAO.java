package com.stronans.domotics.dao;

import com.stronans.domotics.database.DBConnection;
import com.stronans.domotics.database.MeasurementConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by S.King on 06/07/2016.
 * Extensively restructured by S.King on 11/04/2018.
 */
@Repository("TemperatureDAO")
public class TemperatureDAO extends MeasurementConnector {
    @Autowired
    public TemperatureDAO(DBConnection dbConnection) {
        super(dbConnection, "temperature");
    }
}
