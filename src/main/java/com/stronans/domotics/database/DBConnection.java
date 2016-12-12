package com.stronans.domotics.database;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates and serves out JDBC connection to MySQL DB.
 *
 * Created by S.King on 09/07/2016.
 */
@Component
@ConfigurationProperties("com.stronans.domotics")
public final class DBConnection {
    private static final Logger logger = Logger.getLogger(DBConnection.class);

    private Connection connection = null;
    private String host;
    private int port= 3306;
    private String dbName;
    private String userName;
    private String userPassword;

    public DBConnection()
    {
    }

    public void establishConnection()
    {
        String connectionString = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

        try {
            connection = DriverManager.getConnection(connectionString, userName, userPassword);

        } catch (SQLException e) {
            logger.error("Connection to database [" + connectionString +
                    "] using credentials - " + userName + "/" + userPassword + " failed.", e);
        }
    }

    public Connection getConnection()
    {
        if (connection == null) {
            establishConnection();
        }

        return connection;
    }

    public String getFullTableName(String tableName)
    {
        return dbName + "." + tableName;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
