package com.gect.connect.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection class for managing database connectivity using JDBC.
 */
public class DBConnection {
    // Database credentials
    // Note: Change these as per your MySQL configuration
    private static final String URL = "jdbc:mysql://localhost:3306/gect_connect";
    private static final String USER = "root";
    private static final String PASSWORD = "4020"; // Change to your password

    private static Connection connection = null;

    /**
     * Get a connection to the MySQL database.
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Register JDBC driver (optional in newer versions of JDBC)
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to the database successfully!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Close the database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
