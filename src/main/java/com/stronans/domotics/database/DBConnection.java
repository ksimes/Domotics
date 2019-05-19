package com.stronans.domotics.database;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Creates and serves out JDBC connection to MySQL DB.
 * <p>
 * Created by S.King on 09/07/2016.
 */
@Component
@ConfigurationProperties("com.stronans.domotics")
public final class DBConnection {
    private static final Logger logger = Logger.getLogger(DBConnection.class);

    private ArangoDB arangoDB = null;

    private ArangoDatabase ardb = null;
    private String host;
    private int port = 3306;
    private String dbName;
    private String userName;
    private String userPassword;

    private void establishConnection() {
        try {
            arangoDB = new ArangoDB.Builder().host(host, port).user(userName).password(userPassword).maxConnections(4).build();
            ardb = arangoDB.db(dbName);
            logger.info("Made connection to database with url [" + ardb.toString() + "]");

        } catch (ArangoDBException ex) {
            logger.error("Connection to database [" + ardb.toString() +
                    "] using credentials - " + userName + "/" + userPassword + " failed.");
        }
    }

    public ArangoDatabase getConnection() {
        if (ardb == null) {
            establishConnection();
        }

        return ardb;
    }

    public ArangoDB getArangoDB() {
        if (arangoDB == null) {
            establishConnection();
        }

        return arangoDB;
    }

    public void setConnection(ArangoDatabase ardb) {
        this.ardb = ardb;
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
