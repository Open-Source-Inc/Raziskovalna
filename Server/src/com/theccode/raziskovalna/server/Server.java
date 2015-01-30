package com.theccode.raziskovalna.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
    private int port;
    private ServerSocket serverSocket;
    private ArrayList<ConnectionHandler> connectionHandlers;
    private boolean running = false;
    private Thread thread;


    public Server(int port) {
        this.port = port;
        connectionHandlers = new ArrayList<ConnectionHandler>();
    }


    public void run() {
        try {
            running = true;
            serverSocket = new ServerSocket(port);

            while (running) {
                Socket socket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(socket);
                connectionHandlers.add(connectionHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        try {
            serverSocket.close();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void write(String id, String data) {
        clean();
        for (int i = 0; i < connectionHandlers.size(); i++)
            if (connectionHandlers.get(i).id.equals(id))
                connectionHandlers.get(i).write(data);
    }

    public synchronized String getData(String id, String data) {
        clean();
        for (int i = 0; i < connectionHandlers.size(); i++)
            if (connectionHandlers.get(i).id.equals(id))
                return connectionHandlers.get(i).getData(data);

        return null;
    }

    public synchronized int getSize() {
        return connectionHandlers.size();
    }

    private synchronized void clean() {
        for (int i = 0; i < connectionHandlers.size(); i++) {
            if (!connectionHandlers.get(i).connected) {
                connectionHandlers.set(i, null);
                connectionHandlers.remove(i);
                i--;
            }
        }
    }
}