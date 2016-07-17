package com.stronans.domotics.database;

import com.stronans.domotics.model.Measurement;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by S.King on 07/07/2016.
 */
public class ConnectorTest {
    private MeasurementConnectorInterface connector;

    @Before
    public void setUp() throws Exception {
        connector = MeasurementConnector.create("temperature");
    }

    @Test
    public void add() throws Exception {
        Measurement measurement = new Measurement(10, 24.635, "20160707205620");
        measurement = connector.add(measurement);

        assertNotNull(measurement.id());
    }
}