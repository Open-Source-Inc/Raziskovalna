package com.theccode.raziskovalna.server;

import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(80);
        Thread serverThread = new Thread(server);
        serverThread.start();

        DecimalFormat df = new DecimalFormat("#.#");
        while (true) {
            int color = (int)(Math.random() * 3);
            float temp = (float)(Math.random() * 30);

            //server.write("rabbit1", color + "15" + df.format(temp) + " &C");
            String rabbit1Data = server.getData("rabbit1", "temp");
            if (rabbit1Data != null)
                server.write("rabbit1", color + "15" + rabbit1Data + " &C");

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}