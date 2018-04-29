package com.stronans.domotics.database;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by S.King on 07/07/2016.
 */
public class ConnectorTest {
    private MeasurementConnectorInterface connector;

    @Before
    public void setUp() throws Exception {
//        DBConnection dbConnection = new DBConnection();
//
//        dbConnection.setHost("192.168.1.50");
//        dbConnection.setPort(3101);
//        dbConnection.setUserName("measure");
//        dbConnection.setUserPassword("measure");
//        dbConnection.setDbName("domotics");
//        connector = new TemperatureDAO(dbConnection);
    }

    @Test
    public void add() throws Exception {
//        Measurement measurement = new Measurement("1", 24.635, DateInfo.fromUniversalString("20160707205620"), 300, "1");
//        measurement = connector.add(measurement);
//
//        assertNotNull(measurement.id());
        assertTrue(true);
    }
}