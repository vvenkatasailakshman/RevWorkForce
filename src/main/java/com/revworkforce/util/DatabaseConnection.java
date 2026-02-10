package com.revworkforce.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            // Load database properties from external file
            Properties props = new Properties();
            props.load(new FileInputStream("db.properties"));

            URL = props.getProperty("db.url");
            USER = props.getProperty("db.username");
            PASSWORD = props.getProperty("db.password");

            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: Could not load db.properties file.");
            System.err.println("Please ensure db.properties exists in the project root directory.");
            throw new RuntimeException("Database configuration missing.", e);
        } catch (ClassNotFoundException e) {
            System.err.println("CRITICAL ERROR: MySQL JDBC Driver not found.");
            System.err.println("Please ensure the MySQL connector jar is added to the classpath or 'lib' folder.");
            throw new RuntimeException("Database driver missing.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
