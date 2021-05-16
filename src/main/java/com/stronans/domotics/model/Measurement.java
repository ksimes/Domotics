package com.stronans.domotics.model;

import com.arangodb.velocypack.annotations.Expose;
import com.arangodb.velocypack.annotations.SerializedName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stronans.domotics.utilities.DateInfo;

/**
 * Immutable model of measurements value from a specific station.
 * This model will suit all current measurements taken.
 * Created by S.King on 03/07/2016.
 * Restructured for ArangoDB by S.King on 13/04/2018.
 */
public class Measurement {
    protected String id;
    protected String stationId;
    protected double value;
    @Expose(serialize = false, deserialize = true)
    protected DateInfo timeStamp;           // Excluded from Arango Java driver so as to not serialise down to DB.
    @SerializedName("timeStamp")
    protected String timeStampData;         // Set for transfer down to Arango as ISO date format. Not otherwise used.

    protected int sampleRate;
    @Expose(serialize = false, deserialize = true)
    protected boolean status;           // Status only used by UI to check if values should be shown or used as display padding
    protected String sensorType;

    public Measurement() {
    }

    public Measurement(String id, String stationId, double value, DateInfo timeStamp, int sampleRate, String sensorType, boolean status) {
        this.id = id;
        this.stationId = stationId;
        this.timeStamp = timeStamp;
        this.timeStampData = timeStamp.ISOTimestamp();
        this.value = value;
        this.sampleRate = sampleRate;
        this.status = status;
        this.sensorType = sensorType;
    }

    @JsonCreator
    public Measurement(String id, String stationId, double value, String timeStamp, int sampleRate, String sensorType) {
        this(id, stationId, value, DateInfo.fromISOTimestampString(timeStamp), sampleRate, sensorType, true);
    }

    public Measurement(String id, String stationId, double value, DateInfo timeStamp, int sampleRate, String sensorType) {
        this(id, stationId, value, timeStamp, sampleRate, sensorType, true);
    }

    public Measurement(String stationId, double value, DateInfo timeStamp, int sampleRate, String sensorType) {
        this(null, stationId, value, timeStamp, sampleRate, sensorType);
    }

    public Measurement setId(String id) {
        return new Measurement(id, this.stationId, this.value, this.timeStamp, this.sampleRate, this.sensorType);
    }

    public Measurement setDummy(String stationId, DateInfo timeStamp) {
        return new Measurement(null, stationId, 0.0, timeStamp, 0, null, false);
    }

    @JsonProperty("id")
    public String id() {
        return id;
    }

    @JsonProperty("value")
    public double value() {
        return value;
    }

    @JsonProperty("timeStamp")
    public String timeStampString() {
        return DateInfo.toUniversalString(timeStamp);
    }

    public DateInfo timeStamp() {
        return timeStamp;
    }

    @JsonProperty("stationId")
    public String stationId() {
        return stationId;
    }

    @JsonProperty("sampleRate")
    public int sampleRate() {
        return sampleRate;
    }

    @JsonProperty("status")
    public boolean status() {
        return status;
    }

    @JsonProperty("sensorType")
    public String sensorType() {
        return sensorType;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id='" + id + '\'' +
                ", stationId='" + stationId + '\'' +
                ", value=" + value +
                ", timeStamp=" + timeStamp +
                ", timeStampData='" + timeStampData + '\'' +
                ", sampleRate=" + sampleRate +
                ", status=" + status +
                ", sensorType='" + sensorType + '\'' +
                '}';
    }
}
