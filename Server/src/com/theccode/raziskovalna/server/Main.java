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

            server.write("rabbit1", color + "15" + df.format(temp) + " &C");
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}