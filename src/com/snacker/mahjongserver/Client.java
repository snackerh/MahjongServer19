package com.snacker.mahjongserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import com.snacker.MahjongFunction.PlayerStatus;

public class Client extends Thread {

	Socket sock;
	PrintWriter out;
	BufferedReader in;
	Room room;
	String roomid;
	PlayerStatus status;
	int index = -1;
	
	public Client(Socket socket) {
		// TODO Auto-generated constructor stub
		this.sock = socket;
	}
	
	public PrintWriter getPrintWriter() {
		return out;
	}
	
	private void deleteUserIfValid() {
		if (index != -1) {
			room.deleteUser(index);
			if(room.getUserNum() == 0) {
				System.out.println("room <" + roomid + "> deleted!");
				Main.rm.deleteRoom(room);
				index = -1;
			}
		} 		
	}
	
	public PlayerStatus getStatus() {
		return status;
	}
	
	public void run() {
		String msg;
		try {
				out = new PrintWriter(sock.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				
				msg = in.readLine();
				
				String[] array = msg.split("::");
				roomid = array[0];
				index = Integer.parseInt(array[1]);
				
				System.out.println(roomid);
				System.out.println(index);
				
				room = Main.rm.findRoom(roomid);
				if (room == null) {
					room = new Room(roomid);
					Main.rm.addRoom(room);
					System.out.println("added room <" + roomid + ">");
				} else {
					System.out.println("found room <" + roomid + ">");
				}
				
				if(room.getUser(index) != null) {
					System.out.println("Error: Room <" + roomid + ">, user " + index + " already taken");
					index = -1;
					throw new Exception();
				} else {
					room.setUser(index, this);
					status = new PlayerStatus(index);
				}
				
				do {
					msg = in.readLine();
					System.out.println("Got message:" + msg);
					if(msg.equals(MsgType.MSG_STOP.toString())) {
						System.out.println("Info: Room <" + roomid + ">, remove user " + index);
						deleteUserIfValid();
						break;
					} else {
						// do calculation
						if(room.isBlocked()) {
							System.out.println("Room is currently parsing previous command");
						} else {
							room.parseCommand(msg);
						}
					}
				} while(true);
			} catch (SocketException e) {
				System.out.println("Client Connection lost");
				//e.printStackTrace();
			} catch (Exception e) {
				//e.printStackTrace();
			} finally {
				try {
					deleteUserIfValid();
					System.out.println("Sending stop to client");
					out.println("stop");
					out.close();
					in.close();
					sock.close();
				} catch (Exception e) {}
			}
	}
}
