package com.theccode.raziskovalna.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    private int port;
    private boolean run = false;
    private ServerSocket serverSocket;
    private ArrayList<ConnectionHandler> connectionHandlers;

    public void run() {
        try {
            run = true;
            serverSocket = new ServerSocket(port);

            while (run) {
                Socket socket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(socket);
                connectionHandlers.add(connectionHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Server(int port) {
        this.port = port;
        connectionHandlers = new ArrayList<ConnectionHandler>();
    }

    public Server() {
        throw new IllegalArgumentException("Wrong initializer. Use Server(int port);");
    }

    public String read(String id) {
        clean();
        for (int i = 0; i < connectionHandlers.size(); i++) {
            if (connectionHandlers.get(i).getId().equals(id))
                return connectionHandlers.get(i).read();
        }

        return "";
    }

    public void write(String id, String data) {
        clean();
        for (int i = 0; i < connectionHandlers.size(); i++) {
            if (connectionHandlers.get(i).getId().equals(id))
                connectionHandlers.get(i).write(data);
        }
    }

    private void clean() {
        for (int i = 0; i < connectionHandlers.size(); i++) {
            if (!connectionHandlers.get(i).isAlive()) {
                connectionHandlers.remove(i);
                i--;
            }
        }
    }
}