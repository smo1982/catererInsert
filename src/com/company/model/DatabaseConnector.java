package com.company.model;

import java.sql.*;

public class DatabaseConnector {
    private Connection connection = null;
    private Statement statement = null;
    private String url = "jdbc:mysql://localhost:3306/restaurant?user=root";

    private static DatabaseConnector instance;

    public static DatabaseConnector getInstance() {
        if(instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    private DatabaseConnector() {

    }

    private void buildConnection() {
        try {
            String databaseUrl = url;
            connection = DriverManager.getConnection(databaseUrl);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("could not build connection");
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            System.out.println("could not close connection");
            e.printStackTrace();
        }
    }

    public ResultSet fetchData(String sql) {
        buildConnection();
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("could not fetch data");
            e.printStackTrace();
            closeConnection();
        }
        return null;
    }

    public boolean deleteUpdateInsert(String sql) {
        buildConnection();
        try {
            int result = statement.executeUpdate(sql);
            if (result == 0) {
                System.out.println("no matching entry found");
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("could not delete/update/insert data");
            e.printStackTrace();
            return false;
        } finally {
            closeConnection();
        }
    }

}
