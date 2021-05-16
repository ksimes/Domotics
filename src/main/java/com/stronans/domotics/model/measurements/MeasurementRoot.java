package com.stronans.domotics.model.measurements;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stronans.domotics.model.sensors.SensorRoot;
import com.stronans.domotics.model.sensors.SensorSingleReading;
import com.stronans.domotics.utilities.DateInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class MeasurementRoot {
    @JsonProperty("id")
    private String id = UUID.randomUUID().toString();
    @JsonProperty("software")
    protected String software;
    @JsonProperty("cluster")
    protected String cluster;
    @JsonProperty("processor")
    protected String processor;
    @JsonProperty("comments")
    protected String comments;

    @JsonProperty("transport")
    protected Map<String, Object> transport;

    public void fill(MeasurementReading data, SensorSingleReading reading, SensorRoot root, DateInfo date) {
        data.software = root.getSoftware();
        data.cluster = root.getCluster();
        data.processor = root.getProcessor();
        data.comments = root.getComments();
        data.transport = root.getTransport();
    }

    public String id() {
        return id;
    }

    @Override
    public String toString() {
        return "MeasurementRoot{" +
                "id='" + id + '\'' +
                ", software='" + software + '\'' +
                ", cluster='" + cluster + '\'' +
                ", processor='" + processor + '\'' +
                ", comments='" + comments + '\'' +
                ", transport=" + transport +
                '}';
    }
}
