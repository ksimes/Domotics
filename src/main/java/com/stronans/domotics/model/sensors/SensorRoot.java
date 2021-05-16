package com.stronans.domotics.model.sensors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stronans.domotics.model.measurements.MissingAttributeException;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Default model of data gathered from a measurements sensor. Note that there is no time associated with this model as
 * most sensors do not provide this. Original designed for what comes in from the DHT22/ESP8622 station. Changed so that
 * the values are anonymous so that there is no pre-defined bias to what the values represent.
 * <p>
 * This is a POJO so that the JSON can be set automagically by Spring Web
 * <p>
 * <p>
 * Created by S.King on 04/07/2016. In file SensorMeasurement.java
 * Restructured by S.King on 13/09/2020 to provide better data from a sensor site.
 */

@Getter
@Setter
public class SensorRoot {

    @JsonProperty("software")
    private String software;
    @JsonProperty("cluster")
    private String cluster;
    @JsonProperty("processor")
    private String processor;
    @JsonProperty("comments")
    private String comments;

    @JsonProperty("transport")
    private Map<String, Object> transport;

    public SensorRoot() {
    }

    public SensorRoot(String software, String cluster, String processor, String comments, Map<String, Object> transport) {
        this.software = software;
        this.cluster = cluster;
        this.processor = processor;
        this.comments = comments;
        this.transport = transport;
    }

    public void validateHeader(SensorRoot header) {
        if (header.software == null || header.cluster == null) {
            throw new MissingAttributeException("software version or cluster attributes missing from JSON message");
        }
    }

    @Override
    public String toString() {
        return "SensorReadingRoot{" +
                "software='" + software + '\'' +
                ", cluster='" + cluster + '\'' +
                ", processor='" + processor + '\'' +
                ", comments='" + comments + '\'' +
                ", transport=" + transport +
                '}';
    }
}
