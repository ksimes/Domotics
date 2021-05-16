package com.stronans.domotics.database;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.DocumentCreateEntity;
import com.arangodb.velocypack.VPackSlice;
import com.arangodb.velocypack.exception.VPackException;
import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Processes data to and from any of the current measurements tables.
 * These are collections for Temperature, Humidity and HeatIndex (humiture)
 * As these collections all share a common structure they can all be accessed using this class.
 * <p>
 * Created by S.King on 07/07/2016.
 * Extensively restructured for arangoDB by S.King on 10/04/2018.
 */
public abstract class MeasurementConnector implements MeasurementConnectorInterface {
    private static final Logger logger = Logger.getLogger(MeasurementConnector.class);

    private ArangoDatabase database;
    private ArangoCollection collection;
    private String queryPart1;
    private String queryPart2;
    private String countQueryPart1;
    private String countQueryPart2;

    public MeasurementConnector(DBConnection dbConnection, String collectionName) {
        database = dbConnection.getConnection();
        collection = database.collection(collectionName);

        queryPart1 = "FOR m IN " + collectionName;
        queryPart2 = " return m";
        countQueryPart1 = "FOR doc IN " + collectionName;
        countQueryPart2 = " COLLECT WITH COUNT INTO length RETURN length";
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
            final DocumentCreateEntity<Measurement> doc = collection.insertDocument(measurement);

            if (doc != null && doc.getKey() != null) {
                measurement = measurement.setId(doc.getKey());
            }
        } catch (ArangoDBException ae) {
            logger.error("Problem inserting into collection with params : " + measurement, ae);
        } catch (Exception ex) {
            logger.error("Unknown problem executing Query statement ", ex);
        }

        return measurement;
    }

    public List<Measurement> getList() {
        return getList(null, DateInfo.getUndefined(), DateInfo.getUndefined(), false);
    }

    public List<Measurement> getList(String stationId) {
        return getList(stationId, DateInfo.getUndefined(), DateInfo.getUndefined(), false);
    }

    public List<Measurement> getList(String stationId, DateInfo startDate, DateInfo endDate) {
        return getList(stationId, startDate, endDate, false);
    }

    public Measurement getLatest(String stationId) {
        List<Measurement> list = getList(stationId, DateInfo.getUndefined(), DateInfo.getUndefined(), true);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    private String quote(DateInfo dateToQuote) {
        return "'" + dateToQuote.ISOTimestamp() + "'";
    }

    private String quoteDay(DateInfo dateToQuote) {
        return "'" + dateToQuote.ISODate() + "'";
    }

    private String addDates(String preparedQuery, DateInfo startDate, DateInfo endDate) {
        // If both dates are defined then they should be fully defined with date and time.
        if (startDate.isDefined() && endDate.isDefined()) {
            preparedQuery += "Filter m.timeStamp >= " + quote(startDate) +
                    " and m.timestamp <= " + quote(endDate) + " ";

        } else if (startDate.isDefined() && !endDate.isDefined()) {
            preparedQuery += "Filter Date(m.timeStamp) = " + quoteDay(startDate) + " ";
        } else if (!startDate.isDefined() && endDate.isDefined()) {
            preparedQuery += "Filter Date(m.timeStamp) < " + quoteDay(endDate) + " ";
        }

        return preparedQuery;
    }

    @Override
    public List<Measurement> getList(String stationId, DateInfo startDate, DateInfo endDate, boolean latest) {

        String preparedQuery = queryPart1;

        if (stationId != null) {
            preparedQuery += " Filter m.stationId == '" + stationId + "'";
        }

        preparedQuery = addDates(preparedQuery, startDate, endDate);

        // Get the latest value sampled (with station Id gives current value for that room).
        if (latest) {
            preparedQuery += " sort m.timeStamp DESC ";
            preparedQuery += " Limit 1 ";
        } else {
            preparedQuery += " sort m.timeStamp ASC ";
        }


        preparedQuery += queryPart2;

        logger.debug("Query : " + preparedQuery);

        List<Measurement> dbResultSet = getResultsAsList(preparedQuery);

        return getFillValues(dbResultSet);
    }

    /**
     * Get the results of the formed query from the db and store them in a structure suited to the database
     *
     * @param query SQL query formed by other parts of this class
     * @return list of database result objects
     */
    private List<Measurement> getResultsAsList(String query) {
        List<Measurement> resultSet = new ArrayList<>();

        try {
            ArangoCursor<VPackSlice> cursor = database.query(query, null, null, VPackSlice.class);
            cursor.forEachRemaining(aDocument -> {
                logger.trace("aDocument : " + aDocument);
                Measurement measurement = new Measurement(aDocument.get("_key").getAsString(),
                        aDocument.get("stationId").getAsString(),
                        aDocument.get("value").getAsDouble(),
                        DateInfo.fromISOTimestampString(aDocument.get("timeStamp").getAsString()),
                        aDocument.get("sampleRate").getAsInt(),
                        aDocument.get("sensorType").getAsString(),
                        true
                );
                resultSet.add(measurement);
            });
        } catch (ArangoDBException ae) {
            logger.error("Arango problem executing Query statement on collection", ae);
        } catch (VPackException vpe) {
            logger.error("VPack problem getting data from collection", vpe);
        } catch (Exception ex) {
            logger.error("Unknown problem executing Query statement ", ex);
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
    private List<Measurement> getFillValues(List<Measurement> databaseResults) {
        List<Measurement> resultSet = new ArrayList<>();

        for (Measurement dbResultSet : databaseResults) {
            Measurement measurement = new Measurement(dbResultSet.id(), dbResultSet.stationId(),
                    dbResultSet.value(), dbResultSet.timeStamp(), dbResultSet.sampleRate(), dbResultSet.sensorType(), true);
            resultSet.add(measurement);
        }

        return resultSet;
    }

    @Override
    public Long getCount(String stationId, DateInfo startDate, DateInfo endDate) {

        Long result;
        String preparedQuery = countQueryPart1;

        if (stationId != null) {
            preparedQuery += " FILTER doc.stationId == '" + stationId + "'";
        }

        preparedQuery = addDates(preparedQuery, startDate, endDate);

        preparedQuery += countQueryPart2;

        logger.debug("Query : " + preparedQuery);

        result = getCountResult(preparedQuery);

        return result;
    }

    private static Long result = 0L;

    private Long getCountResult(String query) {

        try {
            ArangoCursor<Long[]> cursor = database.query(query, null, null, Long[].class);
            cursor.forEachRemaining(count -> result = count[0]);

        } catch (ArangoDBException ex) {
            logger.error("Problem executing Query all statement ", ex);
        }

        return result;
    }

    public Long getItemCount() {
        return getCount(null, DateInfo.getUndefined(), DateInfo.getUndefined());
    }

    public Long getItemCount(String stationId) {
        return getCount(stationId, DateInfo.getUndefined(), DateInfo.getUndefined());
    }

    public Long getItemCount(String stationId, DateInfo startDate, DateInfo endDate) {
        return getCount(stationId, startDate, endDate);
    }
}
