package com.stronans.domotics.dao;

import com.stronans.domotics.database.DBConnection;
import com.stronans.domotics.model.SensorType;
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
 * Handles the connection to the SensorType connector to draw back SensorType data from the DB.
 * Created by S.King on 06/08/2016.
 */
@Repository
public class SensorTypeDAO {
    private final Logger logger = Logger.getLogger(SensorTypeDAO.class);

    private Connection connection = null;
    private String query;

    @Autowired
    public SensorTypeDAO(DBConnection dbConnection) {
        connection = dbConnection.getConnection();

        String tableName = "sensortype";
        String workingTable = dbConnection.getFullTableName(tableName);

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
