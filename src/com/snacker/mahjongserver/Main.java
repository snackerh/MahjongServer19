package com.snacker.mahjongserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Main {

	static ServerSocket server;
	static RoomManager rm;
	static int port = 9001;
	static Logger logger;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		logger.setLevel(Level.INFO);
		
		Handler handler = new FileHandler("log.log", true);
		LogFormatter formatter = new LogFormatter();
		handler.setFormatter(formatter);
		logger.addHandler(handler);
		
		rm = new RoomManager();
		runServer(port);
	}

	private static void runServer(int port) {
		logger.info("Server: booting up server, port " + port);
		try {
			server = new ServerSocket(port);
			
			while(true) {
				//System.out.println("Server: waiting for new connection");
				
				Socket socket = server.accept();
				Client client = new Client(socket);
				
				client.start();
				logger.info("new connection established");
			}
		} catch (Exception e) {
			logger.severe("ERROR");
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (Exception e) {}
		}
	}
	
}
