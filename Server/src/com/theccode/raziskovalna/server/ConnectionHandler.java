package com.theccode.raziskovalna.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ConnectionHandler implements Runnable{
    private Socket socket;
    private Thread thread;
    private PrintStream out;
    private String id;
    private String lastMessage = "";
    private boolean running = true;
    private boolean write = false;
    private long writeTime = 0;
    private boolean changed = false;

    public ConnectionHandler(Socket socket) {
        id = "";
        this.socket = socket;
        thread = new Thread(this);
        thread.start();

        try {
            out = new PrintStream(socket.getOutputStream(), true, "ASCII");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ASCII"));
            while (running) {
                if (in.ready()) {
                    String inputLine = in.readLine();
                    if (inputLine == null) break;
                    if (id.equals("")) id = inputLine;
                    else lastMessage = inputLine;
                    write = false;
                    changed = true;
                } else if (write && System.currentTimeMillis() - writeTime > 10000) {
                    socket.close();
                    break;
                }

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void stop() {
        running = false;
    }

    public synchronized boolean isAlive() {
        return thread.isAlive();
    }

    public synchronized void write(String data) {
        if (!write) {
            write = true;
            writeTime = System.currentTimeMillis();
        }
        changed = false;
        out.println(data);
    }

    public synchronized boolean getChanged() {
        return changed;
    }

    public synchronized String read() {
        return lastMessage;
    }

    public synchronized String getId() {
        return id;
    }
}