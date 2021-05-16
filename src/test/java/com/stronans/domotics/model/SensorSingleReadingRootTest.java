package com.stronans.domotics.model;

import com.stronans.domotics.model.sensors.SensorRoot;
import com.stronans.domotics.services.mapper.ObjectMapperService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(ObjectMapperService.class)
public class SensorSingleReadingRootTest {
    String SOURCE1_JSON, SOURCE2_JSON, SOURCE3_JSON, SOURCE4_JSON;

    @Autowired
    ObjectMapperService mapper;

    @Before
    public void setUp() throws Exception {
        SOURCE1_JSON = "{\n" +
                "  \"cluster\": \"01\",\n" +
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

        SOURCE3_JSON = "{\n" +
                "  \"version\": \"2.0\",\n" +
                "  \"processor\": \"ESP8266\",\n" +
                "  \"readings\": [\n" +
                "    {\n" +
                "      \"address\": 1,\n" +
                "      \"measureType\": \"TEMPERATURE\",\n" +
                "      \"sensorType\": \"MCP9808\",\n" +
                "      \"sampleRate\": 2000,\n" +
                "      \"payload\": {\n" +
                "        \"resolution\": 3,\n" +
                "        \"celsius\": 20.6875\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        SOURCE4_JSON = "{\n" +
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
                "  \"cluster\": \"01\",\n" +
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
    }

    @Test
    public void getHeaderPropertiesTest_succcess() throws Exception {
        SensorRoot measurement = mapper.getMapper().readValue(SOURCE1_JSON, SensorRoot.class);

        assertThat(measurement.getSoftware(), Matchers.is("2.0"));
        assertThat(measurement.getCluster(), Matchers.is("01"));
        assertThat(measurement.getProcessor(), Matchers.is("ESP8266"));
    }

    @Test
    public void getMalformedHeaderTest_success() throws Exception {
        SensorRoot measurement = mapper.getMapper().readValue(SOURCE3_JSON, SensorRoot.class);

        assertThat(measurement.getSoftware(), Matchers.is("2.0"));
        assertThat(measurement.getCluster(), Matchers.isEmptyOrNullString());
        assertThat(measurement.getProcessor(), Matchers.is("ESP8266"));
    }
}
