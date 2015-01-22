package com.theccode.raziskovalna.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public Server() {
        throw new IllegalArgumentException("Wrong initializer. Use Server(int port);");
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket1 = serverSocket.accept();
            Socket clientSocket2 = serverSocket.accept();

            ConnectionHandler connectionHandler1 = new ConnectionHandler(clientSocket1);
            new Thread(connectionHandler1).start();

            ConnectionHandler connectionHandler2 = new ConnectionHandler(clientSocket2);
            new Thread(connectionHandler2).start();

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}