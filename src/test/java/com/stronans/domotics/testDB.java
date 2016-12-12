package com.stronans.domotics;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Test that database is in place and has the default measure user and password.
 * Taken from website http://www.mkyong.com/jdbc/how-to-connect-to-mysql-with-jdbc-driver-java/
 * Created by S.King on 05/07/2016.
 */
public class testDB {

    @Test
    public void testDb() {

        System.out.println("-------- MySQL JDBC Connection Testing ------------");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;

        try {
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/domotics", "measure", "measure");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.1.50:3306/domotics", "measure", "measure");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

//        assert(connection == null);

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
    }
}
