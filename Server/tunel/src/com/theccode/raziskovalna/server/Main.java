package com.theccode.raziskovalna.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

public class Main {
	public static void main(String args[]) {
		try {
			ServerSocket serverSocket = new ServerSocket(80);
			Socket socket = serverSocket.accept();

			PrintStream out = new PrintStream(socket.getOutputStream(), true, "ASCII");
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ASCII"));

			DecimalFormat decimalFormat = new DecimalFormat("#.#");

			SQL sql = new SQL("localhost", "co2", "password");

			final long TIMEOUT = 10000;

			String id = in.readLine();

			boolean displayTemp = true;

			// Temperature: 18-21, step 3
			// Gas: <600, 600-1000, >1000

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
				sql.write(id, gas, temperature);

				if (displayTemp) {
					float tempNumber = Float.parseFloat(temperature);
					String color = "";
					if (Float.parseFloat(temperature) >= 18 || Float.parseFloat(temperature) <= 21)
						color = "0";
					else if (tempNumber < 18 && tempNumber >= 15 || tempNumber > 21 && tempNumber <= 24)
						color = "1";
					else color = "2";

					out.println(color + temperature + " &C");
					time = System.currentTimeMillis();
					while (true) {
						if (in.ready()) {
							in.readLine();
							break;
						} else if (System.currentTimeMillis() - time > TIMEOUT) {
							socket.close();
							serverSocket.close();
							System.exit(0);
						}
					}

					displayTemp = false;
				} else {
					float gasNumber = Float.parseFloat(gas);
					String color = "";
					if (gasNumber <= 600)
						color = "0";
					else if (gasNumber <= 1000)
						color = "1";
					else color = "2";

					out.println(color + gas + " ppm");
					time = System.currentTimeMillis();
					while (true) {
						if (in.ready()) {
							in.readLine();
							break;
						} else if (System.currentTimeMillis() - time > TIMEOUT) {
							socket.close();
							serverSocket.close();
							System.exit(0);
						}
					}

					displayTemp = true;
				}

				Thread.sleep(5000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}