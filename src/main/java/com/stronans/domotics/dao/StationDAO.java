package com.stronans.domotics.dao;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.velocypack.VPackSlice;
import com.stronans.domotics.database.DBConnection;
import com.stronans.domotics.model.Station;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the connection to the Station connector to draw back station data from the DB.
 * Created by S.King on 14/07/2016.
 * Restructured for ArangoDB by S.King on 10/04/2018.
 */
@Repository
public class StationDAO {
    private final Logger logger = Logger.getLogger(this.getClass());

    private ArangoDatabase database;
    private String query;

    @Autowired
    public StationDAO(DBConnection dbConnection) {
        database = dbConnection.getConnection();
        String tableName = "stations";
        query = "for s in " + tableName;
    }

    private List<Station> getList(long stationId) {
        String preparedQuery = query;

        if (stationId > 0) {
            preparedQuery += " Filter s._key == '" + stationId + "'";
        }

        preparedQuery += " sort s.name ASC";
        preparedQuery += " return s";

        logger.debug("Query : " + preparedQuery);

        return getResultsAsList(preparedQuery);
    }

    private List<Station> getResultsAsList(String query) {
        List<Station> resultSet = new ArrayList<>();

        try {
            ArangoCursor<VPackSlice> cursor = database.query(query, null, null, VPackSlice.class);
            cursor.forEachRemaining(aStation -> {
                Station station = new Station(aStation.get("_key").getAsString(),
                        aStation.get("name").getAsString(),
                        aStation.get("description").getAsString(),
                        aStation.get("type").getAsString());
                resultSet.add(station);
            });
        } catch (ArangoDBException ex) {
            logger.error("Problem executing Query all statement ", ex);
        }

        return resultSet;
    }

    public List<Station> getList() {
        return getList(0);
    }

    public Station getStation(long stationId) {
        List<Station> list = getList(stationId);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
