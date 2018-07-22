package com.stronans.domotics.dao;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.velocypack.VPackSlice;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stronans.domotics.database.DBConnection;
import com.stronans.domotics.model.SensorCache;
import com.stronans.domotics.model.SensorReading;
import com.stronans.domotics.utilities.DateInfo;
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
public class CacheDAO {
    private final Logger logger = Logger.getLogger(this.getClass());

    private ArangoDatabase database;
    private ArangoCollection collection;
    private String query;

    @Autowired
    public CacheDAO(DBConnection dbConnection) {
        database = dbConnection.getConnection();
        String collectionName = "sensorCache";
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
                "sampleRate: c.sampleRates, " +
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
            cursor.forEachRemaining(aReading -> {
                SensorCache sensorItem = new SensorCache(aReading.get("stationId").getAsString(),
                        aReading.get("timeStamp").getAsString(),
                        aReading.get("name").getAsString(),
                        aReading.get("description").getAsString(),
                        aReading.get("temperatureValue").getAsDouble(),
                        aReading.get("humidityValue").getAsDouble(),
                        aReading.get("humitureValue").getAsDouble(),
                        aReading.get("sampleRate").getAsInt(),
                        aReading.get("sensorType").getAsString()
                );
                resultSet.add(sensorItem);
            });
        } catch (ArangoDBException ex) {
            logger.error("Problem executing Query all statement ", ex);
        }

        return resultSet;
    }

    public SensorReading update(SensorReading readingToCache, DateInfo current) {

        CacheStore cacheStore = new CacheStore(readingToCache.getStationId(),
                current.ISOTimestamp(),
                readingToCache.getValue1(),
                readingToCache.getValue2(),
                readingToCache.getValue3(),
                readingToCache.getSampleRate(),
                readingToCache.getSensorType()
        );

        try {
            if (collection.documentExists(cacheStore.stationId())) {
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

    private class CacheStore {
        private final String stationId;
        private final String timeStamp;
        private final double value1;
        private final double value2;
        private final double value3;
        private final int sampleRate;
        private final String sensorType;

        @JsonCreator
        public CacheStore(
                @JsonProperty("stationId") String stationId,
                @JsonProperty("timeStamp") String timeStamp,
                @JsonProperty("temperatureValue") double value1,
                @JsonProperty("humidityValue") double value2,
                @JsonProperty("humitureValue") double value3,
                @JsonProperty("sampleRate") int sampleRate,
                @JsonProperty("sensorType") String sensorType) {
            this.stationId = stationId;
            this.timeStamp = timeStamp;
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.sampleRate = sampleRate;
            this.sensorType = sensorType;
        }

        public String sensorType() {
            return sensorType;
        }

        public String stationId() {
            return stationId;
        }

        public String timeStamp() {
            return timeStamp;
        }

        public double value1() {
            return value1;
        }

        public double value2() {
            return value2;
        }

        public double value3() {
            return value3;
        }

        public int sampleRate() {
            return sampleRate;
        }
    }
}
