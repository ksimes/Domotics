package com.stronans.domotics.database;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by S.King on 09/07/2016.
 */
final class DBConnection {
    private static final Logger logger = Logger.getLogger(DBConnection.class);

    static private Connection connection = null;
    static private String host = "localhost";
    static private String port = "3306";
    static private String dbName = "domotics";
    static private String userName = "measure";
    static private String userPassword = "measure";

    static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            logger.error("MySQL JDBC Driver has not been found");
//            return;
//        }

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
