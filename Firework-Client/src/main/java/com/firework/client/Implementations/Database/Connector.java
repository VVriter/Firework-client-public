package com.firework.client.Implementations.Database;

import java.sql.*;
public class Connector {
    private Connection connection;
    private Statement statement;

    private ResultSet resultSet;
    private String ip;
    private String user;
    private String password;

    public Connector(String ip, String user, String password) {
        this.ip = ip;
        this.user = user;
        this.password = password;
        this.connection = null;
        this.statement = null;
    }

    private void openConnection() {
        try {
            connection = DriverManager.getConnection(ip, user, password);
            statement = connection.createStatement();
        } catch (Exception e) {

        }
    }

    private void closeConnection() {
        try {
            connection.commit();
            connection.close();
            statement.close();

            connection = null;
            statement = null;
        } catch (Exception e) {

        }
    }
    public ResultSet execute(String cmd) {
        openConnection();

        ResultSet rs = null;
        try {
            rs = statement.executeQuery(cmd);
        } catch (Exception e) {

        } finally {
            closeConnection();
            return rs;
        }
    }

}
