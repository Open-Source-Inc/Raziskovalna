package com.theccode.raziskovalna.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

public class Main {
	public static void main(String args[]) throws Exception {
		ServerSocket serverSocket = new ServerSocket(80);
		Socket socket = serverSocket.accept();
		
		PrintStream out = new PrintStream(socket.getOutputStream(), true, "ASCII");
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ASCII"));
		
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		
		SQL sql = new SQL("localhost", "co2", "password");
		
		final long TIMEOUT = 10000;

		while (true) {
			String gas = "";
			String temperature = "";
			
			out.println("temp");
			long time = System.currentTimeMillis();
			while (true) {
				if (in.ready()) {
					temperature = in.readLine();
					break;
				} else if (System.currentTimeMillis() - time > TIMEOUT) {
					socket.close();
					serverSocket.close();
					System.exit(0);
				}
				Thread.sleep(20);
			}
			
			out.println("gas");
			time = System.currentTimeMillis();
			while (true) {
				if (in.ready()) {
					gas = in.readLine();
					break;
				} else if (System.currentTimeMillis() - time > TIMEOUT) {
					socket.close();
					serverSocket.close();
					System.exit(0);
				}
				Thread.sleep(20);
			}
			
			temperature = decimalFormat.format(Float.parseFloat(temperature));
			sql.write("testID", gas, temperature);
			
			Thread.sleep(5000);
		}
	}
}