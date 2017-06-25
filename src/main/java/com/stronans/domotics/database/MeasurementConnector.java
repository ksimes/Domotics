package com.stronans.domotics.database;

import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

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
@Component
public abstract class MeasurementConnector implements MeasurementConnectorInterface {
    private static final Logger logger = Logger.getLogger(MeasurementConnector.class);
    private static final String ANSI_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String ANSI_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Used in the update statement which has a different order/number of parameters than the select
     */
    private static final int STATION_ID = 1;
    private static final int VALUE = 2;
    private static final int TIMESTAMP = 3;
    private static final int SAMPLERATE = 4;
    private static final int SENSORTYPE = 5;

    protected Connection connection = null;
    protected PreparedStatement addStatement;
    protected String query;
    protected String countQuery;

    public MeasurementConnector() {
    }

    /**
     * The addStatement PreparedStatement is set in the implementing class.
     *
     * @param measurement value to write to the database.
     * @return The updated Measurement with the current Id value.
     */
    @Override
    public Measurement add(Measurement measurement) {

        try {
            addStatement.clearParameters();
            addStatement.setLong(STATION_ID, measurement.stationId());
            addStatement.setDouble(VALUE, measurement.value());

            Timestamp timeStamp = new Timestamp(measurement.timeStamp().getMilliseconds());
            addStatement.setTimestamp(TIMESTAMP, timeStamp);
            addStatement.setInt(SAMPLERATE, measurement.sampleRate());
            addStatement.setInt(SENSORTYPE, measurement.sensorType());
            addStatement.executeUpdate();

            logger.debug("Query : " + addStatement.toString());

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

    private String quote(DateInfo dateToQuote) {
        return "\"" + dateToQuote.format(ANSI_TIMESTAMP_FORMAT) + "\"";
    }

    private String quoteDay(DateInfo dateToQuote) {
        return "\"" + dateToQuote.format(ANSI_DATE_FORMAT) + "\"";
    }

    private String addDates(String preparedQuery, DateInfo startDate, DateInfo endDate, boolean whereStarted) {
        // If both dates are defined then they should be fully defined with date and time.
        if (startDate.isDefined() && endDate.isDefined()) {
            preparedQuery += clauseConnector(whereStarted) + " timestamp >= " + quote(startDate) +
                    " AND timestamp <= " + quote(endDate) + " ";

        } else if (startDate.isDefined() && !endDate.isDefined()) {
            preparedQuery += clauseConnector(whereStarted) + " Date(timestamp) = " + quoteDay(startDate) + " ";
        } else if (!startDate.isDefined() && endDate.isDefined()) {
            preparedQuery += clauseConnector(whereStarted) + " Date(timestamp) < " + quoteDay(endDate) + " ";
        }

        return preparedQuery;
    }

    @Override
    public List<Measurement> getList(long stationId, DateInfo startDate, DateInfo endDate, boolean latest) {

        boolean whereStarted = false;

        String preparedQuery = query;

        if (stationId > 0) {
            preparedQuery += clauseConnector(false) + " StationId = " + stationId;
            whereStarted = true;
        }

        preparedQuery = addDates(preparedQuery, startDate, endDate, whereStarted);

        // Get the latest value sampled (with station Id gives current value for that room.
        if (latest) {
            preparedQuery += " ORDER BY timestamp DESC Limit 1";
        }

        logger.debug("Query : " + preparedQuery);

        List<DatabaseResult> dbResultSet = getResultsAsList(preparedQuery);

        return getFillValues(dbResultSet);
    }

    /**
     * Get the results of the formed query from the db and store them in a structure suited to the database
     *
     * @param query SQL query formed by other parts of this class
     * @return list of database result objects
     */
    private List<DatabaseResult> getResultsAsList(String query) {
        List<DatabaseResult> resultSet = new ArrayList<>();

        try {
            Statement queryStatement = connection.createStatement();
            ResultSet rs = queryStatement.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    DateInfo timeStamp = DateInfo.fromLong(rs.getTimestamp(4).getTime());
                    DatabaseResult result = new DatabaseResult(rs.getLong(1), rs.getLong(2),
                            rs.getDouble(3), timeStamp, rs.getInt(5), rs.getInt(6));
                    resultSet.add(result);
                }
            }
            queryStatement.close();
        } catch (SQLException ex) {
            logger.error("Problem executing Query all statement ", ex);
        }

        return resultSet;
    }

    /**
     * Go through the results and based on the sample rate work out of there are any missing values
     * then add in these missing values with markers so they can be displayed differently.
     *
     * @param databaseResults The data as taken from the database
     * @return a list of measurements but real and added for analysis.
     */
    private List<Measurement> getFillValues(List<DatabaseResult> databaseResults) {
        List<Measurement> resultSet = new ArrayList<>();

        for (DatabaseResult dbResultSet : databaseResults) {
            Measurement measurement = new Measurement(dbResultSet.id(), dbResultSet.stationId(),
                    dbResultSet.value(), dbResultSet.timeStamp(), dbResultSet.sampleRate(), dbResultSet.sensorType(), true);
            resultSet.add(measurement);
        }

        return resultSet;
    }

    @Override
    public Long getCount(long stationId, DateInfo startDate, DateInfo endDate) {

        Long result;
        boolean whereStarted = false;

        String preparedQuery = countQuery;

        if (stationId > 0) {
            preparedQuery += clauseConnector(false) + " StationId = " + stationId;
            whereStarted = true;
        }

        preparedQuery = addDates(preparedQuery, startDate, endDate, whereStarted);

        logger.debug("Query : " + preparedQuery);

        result = getCountResult(preparedQuery);

        return result;
    }

    private Long getCountResult(String query) {
        Long result = 0L;

        try {
            Statement queryStatement = connection.createStatement();
            ResultSet rs = queryStatement.executeQuery(query);
            if (rs != null) {
                if (rs.next()) {
                    result = rs.getLong(1);
                }
            }
            queryStatement.close();
        } catch (SQLException ex) {
            logger.error("Problem executing Query all statement ", ex);
        }

        return result;
    }

    private final class DatabaseResult {
        private final Long id;
        private final long stationId;
        private final double value;
        private final DateInfo timeStamp;
        private final int sampleRate;
        private final int sensorType;

        DatabaseResult(Long id, long stationId, double value, DateInfo timeStamp, int sampleRate, int sensorType) {
            this.id = id;
            this.stationId = stationId;
            this.timeStamp = timeStamp;
            this.value = value;
            this.sampleRate = sampleRate;
            this.sensorType = sensorType;
        }

        Long id() {
            return id;
        }

        double value() {
            return value;
        }

        DateInfo timeStamp() {
            return timeStamp;
        }

        long stationId() {
            return stationId;
        }

        int sampleRate() {
            return sampleRate;
        }

        int sensorType() {
            return sensorType;
        }
    }
}
