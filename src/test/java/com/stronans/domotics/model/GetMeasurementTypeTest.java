package com.stronans.domotics.model;

import com.stronans.domotics.services.mapper.ObjectMapperService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Import({ObjectMapperService.class})
public class GetMeasurementTypeTest {
    String SOURCE1_JSON, SOURCE2_JSON;

    @Autowired
    ObjectMapperService mapper;

//    @Autowired
//    private List<SensorDetection> sensorTypes;

    String jsonDataString;

//    @Autowired
//    SensorKindService underTest;

    @Before
    public void setUp() throws Exception {
        SOURCE1_JSON = "{\n" +
                "  \"clusterId\": \"01\",\n" +
                "  \"version\": \"2.0\",\n" +
                "  \"processor\": \"ESP8266\",\n" +
                "  \"readings\": [\n" +
                "    {\n" +
                "      \"address\": 1,\n" +
                "      \"measureType\": \"TEMPERATURE\",\n" +
                "      \"sensorType\": \"DHT11\",\n" +
                "      \"sampleRate\": 5000,\n" +
                "      \"payload\": {\n" +
                "        \"resolution\": 3,\n" +
                "        \"celsius\": 20.6875,\n" +
                "        \"humidity\": 58.0232,\n" +
                "        \"humiture\": 13.1911\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";


        SOURCE2_JSON = "{\n" +
                "  \"clusterId\": \"01\",\n" +
                "  \"version\": \"2.0\",\n" +
                "  \"processor\": \"ESP8266\",\n" +
                "  \"readings\": [\n" +
                "    {\n" +
                "      \"address\": 1,\n" +
                "      \"measureType\": \"ERROR\",\n" +
                "      \"sensorType\": \"DHT11\",\n" +
                "      \"sampleRate\": 5000,\n" +
                "      \"payload\": {\n" +
                "        \"errorMessage\": \"Failure to connect to sensor\",\n" +
                "        \"details\": \"DHT22 not responded\" \n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        jsonDataString = SOURCE1_JSON; // mapper.getMapper().writeValueAsString(serializedSamples);
    }

    @Test
    public void getTemperaturePropertiesTest() throws Exception {
//        SensorMeasurementRoot measurement = mapper.getMapper().readValue(jsonDataString, SensorMeasurementRoot.class);
//        SensorReading returned;
//
//        for (SensorReading measure : measurement.getReadings()) {
//            returned = underTest.select(measure);
//        }
//
//        assertTrue(returned instanceof DHT22Measurement);
//
//        DHT22Measurement dht22Measurement = (DHT22Measurement) returned;
//
//        assertThat(dht22Measurement.getSoftwareVersion(), Matchers.is("2.0"));
//        assertThat(dht22Measurement.getClusterId(), Matchers.is("01"));
//        assertThat(dht22Measurement.getProcessor(), Matchers.is("ESP8266"));
//
//        assertThat(dht22Measurement.getResolution(), CoreMatchers.is(3));
//        assertThat(dht22Measurement.getCelsius(), CoreMatchers.is(20.6875));
//        assertThat(dht22Measurement.getHumidity(), CoreMatchers.is(58.0232));
//        assertThat(dht22Measurement.getHumiture(), CoreMatchers.is(13.1911));
    }
}
