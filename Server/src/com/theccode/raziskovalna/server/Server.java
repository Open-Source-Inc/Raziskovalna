package com.theccode.raziskovalna.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;
    private boolean run = false;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    public Server() {
        throw new IllegalArgumentException("Wrong initializer. Use Server(int port);");
    }

    public void start() {
        try {
            run = true;
            serverSocket = new ServerSocket(port);

            while (run) {
                Socket socket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(socket);
                new Thread(connectionHandler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}