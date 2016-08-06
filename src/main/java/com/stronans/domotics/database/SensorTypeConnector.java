package com.stronans.domotics.database;

import com.stronans.domotics.model.SensorType;
import com.stronans.domotics.model.Station;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Connects to the MySQL database to extract sensor types.
 * Created by S.King on 06/08/2016.
 */
public class SensorTypeConnector {
    private static final Logger logger = Logger.getLogger(SensorTypeConnector.class);
    private static final String tableName = "sensortype";

    private Connection connection = null;
    private String query;

    public SensorTypeConnector() {
        setup();
    }

    private void setup() {
        connection = DBConnection.getConnection();
        String workingTable = DBConnection.getFullTableName(tableName);

        query = "SELECT * FROM " + workingTable;
    }

    public List<SensorType> getList(long sensorId) {
        String preparedQuery = query;

        if (sensorId > 0) {
            preparedQuery += " WHERE id = " + sensorId;
        }

        logger.info("Query : " + preparedQuery);

        return getResultsAsList(preparedQuery);
    }

    private List<SensorType> getResultsAsList(String query) {
        List<SensorType> resultSet = new ArrayList<>();

        try {
            Statement queryStatement = connection.createStatement();
            ResultSet rs = queryStatement.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    SensorType sensorType = new SensorType(rs.getLong(1), rs.getString(2), rs.getString(3));
                    resultSet.add(sensorType);
                }
            }
        } catch (SQLException ex) {
            logger.error("Problem executing Query to collect sensorType data", ex);
        }
        return resultSet;
    }
}
