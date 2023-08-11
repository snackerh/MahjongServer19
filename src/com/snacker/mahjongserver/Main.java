package com.snacker.mahjongserver;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	static ServerSocket server;
	static RoomManager rm;
	static int port = 9001;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		rm = new RoomManager();
		runServer(port);
	}

	private static void runServer(int port) {
		System.out.println("Server: booting up server, port " + port);
		try {
			server = new ServerSocket(port);
			
			while(true) {
				//System.out.println("Server: waiting for new connection");
				
				Socket socket = server.accept();
				Client client = new Client(socket);
				
				client.start();
				System.out.println("Server: new connection established");
			}
		} catch (Exception e) {
			System.out.println("System: ERROR");
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (Exception e) {}
		}
	}
	
}
