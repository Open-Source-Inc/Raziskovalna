package com.theccode.raziskovalna.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable{
    private Socket socket;
    private Thread thread;
    private PrintWriter out;
    private String id;
    private String lastMessage = "";
    private boolean running = true;

    public ConnectionHandler(Socket socket) {
        id = "";
        this.socket = socket;
        thread = new Thread(this);
        thread.start();

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (running) {
                String inputLine = in.readLine();
                if (inputLine == null) break;
                if (id.equals("")) id = inputLine;
                else lastMessage = inputLine;

                System.out.println(inputLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean isAlive() {
        return thread.isAlive();
    }

    public synchronized void write(String data) {
        out.println(data);
    }

    public synchronized String read() {
        return lastMessage;
    }

    public synchronized String getId() {
        return id;
    }
}