package com.firework.client.Implementations.Database;

import java.sql.*;
public class Connector {
    private Connection connection;
    private Statement statement;
    private String ip;
    private String user;
    private String password;

    public Connector(String ip, String user, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");

        this.ip = ip;
        this.user = user;
        this.password = password;
        this.connection = DriverManager.getConnection(ip, user, password);
        this.statement = connection.createStatement();
    }

    public ResultSet execute(String cmd) {
        ResultSet result = this.statement.executeQuery(cmd);
        return result;
    }
}
