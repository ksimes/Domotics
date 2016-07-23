package com.stronans.domotics.database;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.model.Station;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes data to and from any of the current measurement tables.
 * These are tables for Temperature, Humidity and HeatIndex (humiture)
 * As these tables all share a common table structure they can all be accessed using this class.
 * <p>
 * Created by S.King on 07/07/2016.
 */
public class MeasurementConnector implements MeasurementConnectorInterface {
    private static final Logger logger = Logger.getLogger(MeasurementConnector.class);
    private static final String ANSI_DATE_FORMAT = "";

    private static final int STATION_ID = 1;
    private static final int VALUE = 2;
    private static final int TIMESTAMP = 3;

    private Connection connection = null;
    private PreparedStatement addStatement;
    private String query;

    private MeasurementConnector(String tableName) {

        connection = DBConnection.getConnection();
        String workingTable = DBConnection.getFullTableName(tableName);

        try {
            addStatement = connection.prepareStatement(
                    "INSERT INTO " + workingTable +
                            " (stationId, value, timestamp)" +
                            " VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            logger.error("Problem creating add prepared statement.", e);
        }

        query = "SELECT * FROM " + workingTable;
    }

    static public MeasurementConnectorInterface create(String tableName) {
        return new MeasurementConnector(tableName);
    }

    @Override
    public Measurement add(Measurement measurement) {

        try {
            addStatement.clearParameters();
            addStatement.setLong(STATION_ID, measurement.stationId());
            addStatement.setDouble(VALUE, measurement.value());

            Timestamp timeStamp = new Timestamp(measurement.timeStamp().getMilliseconds());
            addStatement.setTimestamp(TIMESTAMP, timeStamp);
            addStatement.executeUpdate();

            ResultSet rs = addStatement.getGeneratedKeys();
            if (rs != null && rs.next()) {
                measurement = measurement.setId(rs.getLong(1));
            }
        } catch (SQLException ex) {
            logger.error("Problem executing add prepared statement with params : " + measurement, ex);
        }

        return measurement;
    }

    private String clauseConnector(boolean whereClauseStarted) {
        String result = " WHERE ";

        if (whereClauseStarted) {
            result = " AND ";
        }

        return result;
    }

    private String quote(DateInfo dateToQuote)
    {
        final String FORMAT = "yyyy-MM-dd";

        return "\"" + dateToQuote.format(FORMAT) + "\"";
    }

    @Override
    public List<Measurement> getList(long stationId, DateInfo startDate, DateInfo endDate, boolean latest) {

        List<Measurement> resultSet = new ArrayList<>();
        boolean whereStarted = false;

        String preparedQuery = query;

        if (stationId > 0) {
            preparedQuery += clauseConnector(whereStarted) + " StationId = " + stationId;
            whereStarted = true;
        }

        // If both dates are defined then they should be fully defined with date and time.
        if (startDate.isDefined() && endDate.isDefined()) {
            preparedQuery += clauseConnector(whereStarted) + " timestamp >= " + quote(startDate) +
                    " AND timestamp <= " + quote(startDate) + " ";

        } else if (startDate.isDefined() && !endDate.isDefined()) {
            preparedQuery += clauseConnector(whereStarted) + " Date(timestamp) = " + quote(startDate) + " ";
        } else if (!startDate.isDefined() && endDate.isDefined()) {
            preparedQuery += clauseConnector(whereStarted) + " Date(timestamp) < " + quote(endDate) + " ";
        }

        // Get the latest value sampled (with station Id gives current value for that room.
        if (latest) {
            preparedQuery += " ORDER BY timestamp DESC Limit 1";
        }

        logger.debug("Query : " + preparedQuery);

        resultSet = getResultsAsList(preparedQuery);

        return resultSet;
    }

    private List<Measurement> getResultsAsList(String query) {
        List<Measurement> resultSet = new ArrayList<>();

        try {
            Statement queryStatement = connection.createStatement();
            ResultSet rs = queryStatement.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    DateInfo timeStamp = DateInfo.fromLong(rs.getTimestamp(4).getTime());
                    Measurement measurement = new Measurement(rs.getLong(1), rs.getLong(2), rs.getDouble(3), timeStamp);
                    resultSet.add(measurement);
                }
            }
            queryStatement.close();
        } catch (SQLException ex) {
            logger.error("Problem executing Query all statement ", ex);
        }

        return resultSet;
    }
}
