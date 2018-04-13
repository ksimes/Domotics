package com.stronans.domotics.dao;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.velocypack.VPackSlice;
import com.stronans.domotics.database.DBConnection;
import com.stronans.domotics.model.SensorType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the connection to the SensorType connector to draw back SensorType data from the DB.
 * Created by S.King on 06/08/2016.
 * Restructured for ArangoDB by S.King on 10/04/2018.
 */
@Repository
public class SensorTypeDAO {
    private final Logger logger = Logger.getLogger(SensorTypeDAO.class);

    private ArangoDatabase database;
    private String query;

    @Autowired
    public SensorTypeDAO(DBConnection dbConnection) {
        database = dbConnection.getConnection();

        String tableName = "sensorTypes";
        query = "for s in " + tableName;
    }

    private List<SensorType> getList(long sensorId) {
        String preparedQuery = query;

        if (sensorId > 0) {
            preparedQuery += " Filter s._key == '" + sensorId + "'";
        }

        preparedQuery += " sort s.name ASC";
        preparedQuery += " return s";

        logger.info("Query : " + preparedQuery);

        return getResultsAsList(preparedQuery);
    }

    private List<SensorType> getResultsAsList(String query) {
        List<SensorType> resultSet = new ArrayList<>();

        try {
            ArangoCursor<VPackSlice> cursor = database.query(query, null, null, VPackSlice.class);
            cursor.forEachRemaining(aSensorType -> {
                SensorType sensorType = new SensorType(aSensorType.get("_key").getAsString(),
                        aSensorType.get("name").getAsString(),
                        aSensorType.get("description").getAsString());
                resultSet.add(sensorType);
            });
        } catch (ArangoDBException ex) {
            logger.error("Problem executing Query all statement ", ex);
        }

        return resultSet;
    }

    @Cacheable("sensorList")
    public List<SensorType> getList() {
        return getList(0);
    }

    public SensorType getSensorType(long stationId) {
        List<SensorType> list = getList(stationId);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
