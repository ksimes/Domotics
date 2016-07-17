package com.stronans.domotics.database;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.model.Station;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by S.King on 15/07/2016.
 */
public class StationConnector {
    private static final Logger logger = Logger.getLogger(StationConnector.class);
    private static final String tableName = "stations";

    private Connection connection = null;
    private String query;

    public StationConnector() {
        connection = DBConnection.getConnection();
        String workingTable = DBConnection.getFullTableName(tableName);

        query = "SELECT * FROM " + workingTable;
    }

    public List<Station> getList(long stationId) {
        List<Station> resultSet = new ArrayList<>();

        String preparedQuery = query;

        if(stationId > 0 )
        {
            preparedQuery += " WHERE id = " + stationId;
        }

        logger.debug("Query : " + preparedQuery);
        try {
            Statement queryStatement = connection.createStatement();
            ResultSet rs = queryStatement.executeQuery(preparedQuery);
            resultSet = getResultsAsList(rs);
        } catch (SQLException ex) {
            logger.error("Problem executing Query all statement ", ex);
        }

        return resultSet;
    }

    private List<Station> getResultsAsList(ResultSet rs) throws SQLException {
        List<Station> resultSet = new ArrayList<>();
        if (rs != null) {
            while (rs.next()) {
                Station station = new Station(rs.getLong(1), rs.getString(2), rs.getString(3));
                resultSet.add(station);
            }
        }
        return resultSet;
    }
}
