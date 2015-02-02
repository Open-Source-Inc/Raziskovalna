package com.theccode.raziskovalna.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ConnectionHandler {
    private Socket socket;
    private PrintStream out;
    private BufferedReader in;
    private long timeout = 15000;

    public boolean connected = true;
    public String id = "";


    public ConnectionHandler(Socket socket) {
        try {
            this.socket = socket;
            out = new PrintStream(this.socket.getOutputStream(), true, "ASCII");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ASCII"));

            long start = System.currentTimeMillis();
            while (true) {
                if (in.ready()) {
                    id = in.readLine();
                    break;
                } else if (System.currentTimeMillis() - start > timeout) {
                    connected = false;
                    socket.close();
                    break;
                }
                Thread.sleep(1000);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String data) {
        try {
            out.println(data);
            long time = System.currentTimeMillis();
            while (true) {
                if (in.ready()) {
                    in.readLine();
                    return;
                } else if (System.currentTimeMillis() - time > timeout) {
                    connected = false;
                    socket.close();
                    return;
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getData(String data) {
        try {
            out.println(data);
            long time = System.currentTimeMillis();
            while (true) {
                if (in.ready())
                    return in.readLine();
                else if (System.currentTimeMillis() - time > timeout) {
                    connected = false;
                    socket.close();
                    return null;
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}