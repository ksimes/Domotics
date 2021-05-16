package com.stronans.domotics.database;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Creates and serves out JDBC connection to MySQL DB.
 * <p>
 * Created by S.King on 09/07/2016.
 * Updated by S.King on 24/12/2020.
 */
@Component
@ConfigurationProperties("com.stronans.domotics")
@Slf4j
@Setter
public final class DBConnection {
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
            log.info("Made connection to database {} with url [{}:{}]", dbName, host, port);

        } catch (ArangoDBException ex) {
            log.error("Connection to database {} with url [{}:{}] using credentials - {}/{} failed.",
                    dbName, host, port, userName, userPassword);
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
}
