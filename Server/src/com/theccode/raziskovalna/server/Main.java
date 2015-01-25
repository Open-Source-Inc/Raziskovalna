package com.theccode.raziskovalna.server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(80);
        Thread serverThread = new Thread(server);
        serverThread.start();

        while (true) {
            server.write("rabbit1", "20.3 " + (char)194 + "C");
            server.write("mami", "20.3 " + (char)248 + "C");
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}