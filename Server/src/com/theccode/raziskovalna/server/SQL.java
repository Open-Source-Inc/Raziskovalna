package com.theccode.raziskovalna.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQL {
    private String url, user, password;

    public SQL(String ip, String user, String password) {
        url = "jdbc:mysql://" + ip + ":3306/co2";
        this.user = user;
        this.password = password;
    }

    public void write(String id, String gas, String temperature) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            String query = "insert into data values('" + id + "', " + gas + ", " + temperature + ")";
            statement.executeUpdate(query);

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}