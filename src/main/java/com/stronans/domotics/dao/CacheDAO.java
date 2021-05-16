package com.stronans.domotics.dao;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.velocypack.VPackSlice;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stronans.domotics.database.DBConnection;
import com.stronans.domotics.model.sensors.SensorCache;
import com.stronans.domotics.model.sensors.SensorMeasurement;
import com.stronans.domotics.model.sensors.SensorReadingSingleEntry;
import com.stronans.domotics.utilities.DateInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the connection to the cache connector to draw back sensorCache data from the DB.
 * Created by S.King on 21/07/2018.
 */
@Repository
@Slf4j
public class CacheDAO {
    private final Logger logger = Logger.getLogger(this.getClass());

    private ArangoDatabase database;
    private ArangoCollection collection;
    private String query;

    @Autowired
    public CacheDAO(DBConnection dbConnection) {
        database = dbConnection.getConnection();
        String collectionName = "sensorcache";
        collection = database.collection(collectionName);
        query = "FOR s IN stations " +
                "FOR c IN sensorcache " +
                "FILTER s._key == c._key " +
                "sort s.name " +
                "RETURN { stationId: s._key, name: s.name, description: s.description, " +
                "timeStamp: c.timeStamp, " +
                "temperatureValue: c.temperatureValue, " +
                "humidityValue: c.humidityValue, " +
                "humitureValue: c.humitureValue, " +
                "sampleRate: c.sampleRate, " +
                "sensorType: s.type}";

    }

    public List<SensorCache> getList() {
        String preparedQuery = query;

        logger.debug("Query : " + preparedQuery);

        return getResultsAsList(preparedQuery);
    }

    private List<SensorCache> getResultsAsList(String query) {
        List<SensorCache> resultSet = new ArrayList<>();

        try {
            ArangoCursor<VPackSlice> cursor = database.query(query, null, null, VPackSlice.class);
            cursor.forEachRemaining(cacheRecord -> {
                SensorCache sensorItem = new SensorCache(cacheRecord.get("stationId").getAsString(),
                        cacheRecord.get("name").getAsString(),
                        cacheRecord.get("description").getAsString(),
                        cacheRecord.get("timeStamp").getAsString(),
                        cacheRecord.get("temperatureValue").getAsDouble(),
                        cacheRecord.get("humidityValue").getAsDouble(),
                        cacheRecord.get("humitureValue").getAsDouble(),
                        cacheRecord.get("sampleRate").getAsInt(),
                        cacheRecord.get("sensorType").getAsString()
                );
                resultSet.add(sensorItem);
            });
        } catch (ArangoDBException ex) {
            logger.error("Problem executing Query all statement ", ex);
        }

        return resultSet;
    }

    public SensorMeasurement update(SensorMeasurement readingToCache, DateInfo current) {

        CacheStore cacheStore = new CacheStore(readingToCache.getStationId(),
                current.ISOTimestamp(),
                readingToCache.getValue1(),
                readingToCache.getValue2(),
                readingToCache.getValue3(),
                readingToCache.getSampleRate(),
                readingToCache.getSensorType()
        );

        try {
            if (!collection.documentExists(cacheStore.stationId())) {
                collection.insertDocument(cacheStore);
            } else {
                collection.updateDocument(cacheStore.stationId(), cacheStore);
            }
        } catch (ArangoDBException ae) {
            logger.error("Problem inserting/updating collection with params : " + readingToCache, ae);
        } catch (Exception ex) {
            logger.error("Unknown problem executing cache update ", ex);
        }

        return readingToCache;
    }

    public SensorReadingSingleEntry updateNew(SensorReadingSingleEntry readingToStore) {

        try {
            if (!collection.documentExists(readingToStore.get_key())) {
                collection.insertDocument(readingToStore);
            } else {
                collection.updateDocument(readingToStore.get_key(), readingToStore);
            }
        } catch (ArangoDBException ae) {
            log.error("Problem inserting/updating collection with params : {} ", readingToStore, ae);
        } catch (Exception ex) {
            logger.error("Unknown problem executing cache update ", ex);
        }

        return readingToStore;
    }

    private class CacheStore {
        private final String _key;
        private final String stationId;
        private final String timeStamp;
        private final double temperatureValue;
        private final double humidityValue;
        private final double humitureValue;
        private final int sampleRate;
        private final String sensorType;

        @JsonCreator
        public CacheStore(
                @JsonProperty("stationId") String stationId,
                @JsonProperty("timeStamp") String timeStamp,
                @JsonProperty("temperatureValue") double temperatureValue,
                @JsonProperty("humidityValue") double humidityValue,
                @JsonProperty("humitureValue") double humitureValue,
                @JsonProperty("sampleRate") int sampleRate,
                @JsonProperty("sensorType") String sensorType) {
            this.stationId = stationId;
            this._key = stationId;
            this.timeStamp = timeStamp;
            this.temperatureValue = temperatureValue;
            this.humidityValue = humidityValue;
            this.humitureValue = humitureValue;
            this.sampleRate = sampleRate;
            this.sensorType = sensorType;
        }

        @JsonProperty("_key")
        public String _key() {
            return _key;
        }

        @JsonProperty("sensorType")
        public String sensorType() {
            return sensorType;
        }

        @JsonProperty("stationId")
        public String stationId() {
            return stationId;
        }

        @JsonProperty("timeStamp")
        public String timeStamp() {
            return timeStamp;
        }

        @JsonProperty("temperatureValue")
        public double value1() {
            return temperatureValue;
        }

        @JsonProperty("humidityValue")
        public double value2() {
            return humidityValue;
        }

        @JsonProperty("humitureValue")
        public double value3() {
            return humitureValue;
        }

        @JsonProperty("sampleRate")
        public int sampleRate() {
            return sampleRate;
        }
    }
}
