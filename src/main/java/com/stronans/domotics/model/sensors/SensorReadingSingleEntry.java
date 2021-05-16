package com.stronans.domotics.model.sensors;

import com.arangodb.velocypack.annotations.Expose;
import com.arangodb.velocypack.annotations.SerializedName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stronans.domotics.utilities.DateInfo;
import lombok.Getter;

@Getter
public class SensorReadingSingleEntry extends SensorRoot {
    @Expose(serialize = false, deserialize = true)
    private DateInfo timeStamp;           // Excluded from Arango Java driver so as to not serialise down to DB.
    @SerializedName("timeStamp")
    private String timeStampData;         // Set for transfer down to Arango as ISO date format. Not otherwise used.
    @JsonProperty("reading")
    private SensorSingleReading reading;
    @JsonProperty("_key")
    private String _key;

    @JsonProperty("timeStamp")
    public String timeStampString() {
        return DateInfo.toUniversalString(timeStamp);
    }

    public SensorReadingSingleEntry() {
    }

    public SensorReadingSingleEntry(DateInfo timeStamp) {
        this.timeStamp = timeStamp;
    }

    public SensorReadingSingleEntry(SensorSingleReading sensorSingleReading, SensorRoot sensorRoot, DateInfo timeStamp) {
        this.setCluster(sensorRoot.getCluster());
        this.setSoftware(sensorRoot.getSoftware());
        this.setProcessor(sensorRoot.getProcessor());
        this.setComments(sensorRoot.getComments());
        this.setTransport(sensorRoot.getTransport());

        this.reading = sensorSingleReading;
        this._key = sensorRoot.getCluster() + String.format("%04d", sensorSingleReading.getAddress());
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "SensorReadingSingleEntry{" +
                super.toString() +
                " timeStamp=" + timeStamp +
                '}';
    }
}
