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

    private static final int STATION_ID = 1;
    private static final int VALUE = 2;
    private static final int TIMESTAMP = 3;

    protected Connection connection = null;
    protected PreparedStatement addStatement;
    protected String query;
    protected String countQuery;

    public MeasurementConnector() {
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

        List<Measurement> resultSet = new ArrayList<>();
        boolean whereStarted = false;

        String preparedQuery = query;

        if (stationId > 0) {
            preparedQuery += clauseConnector(whereStarted) + " StationId = " + stationId;
            whereStarted = true;
        }

        preparedQuery = addDates(preparedQuery, startDate, endDate, whereStarted);

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

    @Override
    public Long getCount(long stationId, DateInfo startDate, DateInfo endDate) {

        Long result;
        boolean whereStarted = false;

        String preparedQuery = countQuery;

        if (stationId > 0) {
            preparedQuery += clauseConnector(whereStarted) + " StationId = " + stationId;
            whereStarted = true;
        }

        preparedQuery = addDates(preparedQuery, startDate, endDate, whereStarted);

        logger.debug("Query : " + preparedQuery);

        result = getCountResult(preparedQuery);

        return result;
    }

    private Long getCountResult(String query) {
        Long result = new Long(0);

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
}
