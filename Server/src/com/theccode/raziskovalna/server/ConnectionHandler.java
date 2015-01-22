package com.theccode.raziskovalna.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionHandler implements Runnable{
    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}