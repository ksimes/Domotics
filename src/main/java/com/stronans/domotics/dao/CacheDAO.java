package com.stronans.domotics.dao;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.DocumentCreateEntity;
import com.arangodb.velocypack.VPackSlice;
import com.stronans.domotics.database.DBConnection;
import com.stronans.domotics.model.SensorReading;
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
                "temperatureValue: c.temperatureValue, " +
                "humidityValue: c.humidityValue, " +
                "humitureValue: c.humitureValue, " +
                "sampleRate: c.sampleRates, " +
                "sensorType: s.type}";

    }

    public List<SensorReading> getList() {
        String preparedQuery = query;

        logger.debug("Query : " + preparedQuery);

        return getResultsAsList(preparedQuery);
    }

    private List<SensorReading> getResultsAsList(String query) {
        List<SensorReading> resultSet = new ArrayList<>();

        try {
            ArangoCursor<VPackSlice> cursor = database.query(query, null, null, VPackSlice.class);
            cursor.forEachRemaining(aReading -> {
                SensorReading sensorReading = new SensorReading(aReading.get("stationId").getAsString(),
                        aReading.get("temperatureValue").getAsDouble(),
                        aReading.get("humidityValue").getAsDouble(),
                        aReading.get("humitureValue").getAsDouble(),
                        aReading.get("sampleRate").getAsInt(),
                        aReading.get("sensorType").getAsString()
                );
                resultSet.add(sensorReading);
            });
        } catch (ArangoDBException ex) {
            logger.error("Problem executing Query all statement ", ex);
        }

        return resultSet;
    }

    public SensorReading update(SensorReading readingToCache) {
        DocumentCreateEntity<SensorReading> doc;

        try {
            if (collection.documentExists(readingToCache.getStationId())) {
                collection.insertDocument(readingToCache);
            } else {
                collection.updateDocument(readingToCache.getStationId(), readingToCache);
            }
        } catch (ArangoDBException ae) {
            logger.error("Problem inserting/updating collection with params : " + readingToCache, ae);
        } catch (Exception ex) {
            logger.error("Unknown problem executing cache update ", ex);
        }

        return readingToCache;
    }
}
