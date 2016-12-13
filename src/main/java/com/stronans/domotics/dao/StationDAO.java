package com.stronans.domotics.dao;

import com.stronans.domotics.database.DBConnection;
import com.stronans.domotics.model.Station;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the connection to the Station connector to draw back station data from the DB.
 * Created by S.King on 14/07/2016.
 */
@Repository
public class StationDAO {
    private final Logger logger = Logger.getLogger(this.getClass());

    private Connection connection = null;
    private String query;

    @Autowired
    private void StationDAO(DBConnection dbConnection) {
        connection = dbConnection.getConnection();

        String tableName = "stations";
        String workingTable = dbConnection.getFullTableName(tableName);

        query = "SELECT * FROM " + workingTable;
    }

    public List<Station> getList(long stationId) {
        String preparedQuery = query;

        if (stationId > 0) {
            preparedQuery += " WHERE id = " + stationId;
        }

        logger.debug("Query : " + preparedQuery);

        return getResultsAsList(preparedQuery);
    }

    private List<Station> getResultsAsList(String query) {
        List<Station> resultSet = new ArrayList<>();

        try {
            Statement queryStatement = connection.createStatement();
            ResultSet rs = queryStatement.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    Station station = new Station(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                    resultSet.add(station);
                }
            }
        } catch (SQLException ex) {
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
