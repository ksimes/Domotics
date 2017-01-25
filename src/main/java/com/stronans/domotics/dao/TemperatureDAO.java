package com.stronans.domotics.dao;

import com.stronans.domotics.database.DBConnection;
import com.stronans.domotics.database.MeasurementConnector;
import com.stronans.domotics.model.Measurement;
import com.stronans.domotics.utilities.DateInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by S.King on 06/07/2016.
 */
@Repository("TemperatureDAO")
public class TemperatureDAO extends MeasurementConnector {
    private static final Logger logger = Logger.getLogger(TemperatureDAO.class);

    @Autowired
    public TemperatureDAO(DBConnection dbConnection) {
        connection = dbConnection.getConnection();

        String TABLE_NAME = "temperature";
        String workingTable = dbConnection.getFullTableName(TABLE_NAME);

        try {
            addStatement = connection.prepareStatement(
                    "INSERT INTO " + workingTable +
                            " (stationId, value, timestamp)" +
                            " VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            logger.error("Problem creating add prepared statement.", e);
        }

        query = "SELECT * FROM " + workingTable;
        countQuery = "SELECT count(*) FROM " + workingTable;
    }

    public List<Measurement> getList() {
        return getList(0, DateInfo.getUndefined(), DateInfo.getUndefined(), false);
    }

    public List<Measurement> getList(long stationId) {
        return getList(stationId, DateInfo.getUndefined(), DateInfo.getUndefined(), false);
    }

    public List<Measurement> getList(long stationId, DateInfo startDate, DateInfo endDate) {
        return getList(stationId, startDate, endDate, false);
    }

    public Measurement getLatest(long stationId) {
        List<Measurement> list = getList(stationId, DateInfo.getUndefined(), DateInfo.getUndefined(), true);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public Long getItemCount() {
        return getCount(0, DateInfo.getUndefined(), DateInfo.getUndefined());
    }

    public Long getItemCount(long stationId) {
        return getCount(stationId, DateInfo.getUndefined(), DateInfo.getUndefined());
    }

    public Long getItemCount(long stationId, DateInfo startDate, DateInfo endDate) {
        return getCount(stationId, startDate, endDate);
    }
}
