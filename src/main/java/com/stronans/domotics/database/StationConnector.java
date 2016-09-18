package com.stronans.domotics.database;

import com.stronans.domotics.model.Station;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Connects to the MySQL database to extract station data.
 * Created by S.King on 15/07/2016.
 */
public class StationConnector {
    private static final Logger logger = Logger.getLogger(StationConnector.class);
    private static final String tableName = "stations";

    private Connection connection = null;
    private String query;

    public StationConnector() {
        setup();
    }

    private void setup() {
        connection = DBConnection.getConnection();
        String workingTable = DBConnection.getFullTableName(tableName);

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
}
