package com.stronans.domotics.database;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates and serves out JDBC connection to MySQL DB.
 *
 * Created by S.King on 09/07/2016.
 */
final class DBConnection {
    private static final Logger logger = Logger.getLogger(DBConnection.class);

    static private Connection connection = null;
    @Value("${com.stronans.domotics.host}")
    static private String host="localhost";
    @Value("${com.stronans.domotics.port}")
    static private String port="3306";
    @Value("${com.stronans.domotics.dbName}")
    static private String dbName="domotics";
    @Value("${com.stronans.domotics.userName}")
    static private String userName="measure";
    @Value("${com.stronans.domotics.userPassword}")
    static private String userPassword="measure";

    static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            logger.error("MySQL JDBC Driver has not been found");
//            return;
//        }
        establishConnection();
    }

    static public void establishConnection()
    {
        try {
            String connectionString = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

            connection = DriverManager.getConnection(connectionString, userName, userPassword);

        } catch (SQLException e) {
            logger.error("Connection to database " + dbName + " using credentials - " + userName + "/" + userPassword + " failed.", e);
        }
    }

    static public Connection getConnection()
    {
        if (connection == null) {
            logger.error("Connection to database " + dbName + " returned null.");
        }

        return connection;
    }

    static public String getFullTableName(String tableName)
    {
        return dbName + "." + tableName;
    }
}
