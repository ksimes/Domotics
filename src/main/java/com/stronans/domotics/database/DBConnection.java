package com.stronans.domotics.database;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates and serves out JDBC connection to MySQL DB.
 *
 * Created by S.King on 09/07/2016.
 */
@ConfigurationProperties("com.stronans.domotics")
public final class DBConnection {
    private static final Logger logger = Logger.getLogger(DBConnection.class);

    static private Connection connection = null;
    static private String host="localhost";
    static private int port= 3306;
    static private String dbName="domotics";
    static private String userName="measure";
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

    public static void setConnection(Connection connection) {
        DBConnection.connection = connection;
    }

    public static void setHost(String host) {
        DBConnection.host = host;
    }

    public static void setPort(int port) {
        DBConnection.port = port;
    }

    public static void setDbName(String dbName) {
        DBConnection.dbName = dbName;
    }

    public static void setUserName(String userName) {
        DBConnection.userName = userName;
    }

    public static void setUserPassword(String userPassword) {
        DBConnection.userPassword = userPassword;
    }
}
