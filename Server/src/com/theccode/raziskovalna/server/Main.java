package com.theccode.raziskovalna.server;

import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(80);
        server.start();

        DecimalFormat df = new DecimalFormat("#.#");
        while (true) {
            int color = (int)(Math.random() * 3);
            float temp;

            String rabbit1Data = server.getData("rabbit1", "temp");
            if (rabbit1Data != null) {
                try {
                    temp = Float.parseFloat(rabbit1Data);
                    server.write("rabbit1", color + "15" + df.format(temp) + " &C");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}