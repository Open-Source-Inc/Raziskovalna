package com.theccode.raziskovalna.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;

public class SQL {
    private String url, user, password;

    public SQL(String ip, String user, String password) {
        url = "jdbc:mysql://" + ip + ":3306/raziskovalna";
        this.user = user;
        this.password = password;
    }

    public void write(String id, String gas, String temperature) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            long time = Instant.now().getEpochSecond() - 1420070400;

            String query = "insert into data values('" + id + "', " + gas + ", " + temperature + ", " + time + ")";
            statement.executeUpdate(query);

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet read(String id, int timeInterval) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            long time = Instant.now().getEpochSecond() - 1420070400;
            long minTime = time - timeInterval;

            String query = "select * from data where time > " + minTime + " and id = '" + id + "'";
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("id : gas : temperature : time");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + " : " + resultSet.getInt(2) + " : " + resultSet.getFloat(3) + " : " + resultSet.getLong(4));
            }

            return resultSet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}