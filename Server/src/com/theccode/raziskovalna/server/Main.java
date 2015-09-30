package com.theccode.raziskovalna.server;

import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();

        SQL sql = new SQL("192.168.1.63", "raziskovalna", "Raziskovalna123");

        DecimalFormat df = new DecimalFormat("#.#");
        while (true) {

            String temperature = server.getData("rabbit1", "temp");
            String gas = server.getData("rabbit1", "gas");

            if (temperature != null && gas != null) {
                temperature = df.format(Float.parseFloat(temperature));
                sql.write("rabbit1", gas, temperature);
            }

            sql.read("rabbit1", 300);

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}