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
		String id;
		try {
				out = new PrintWriter(sock.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				
				msg = in.readLine();
				
				String[] array = msg.split("\\|");
				id = array[0];
				roomid = array[1];
				index = Integer.parseInt(array[2]);
				
				//System.out.println(roomid);
				//System.out.println(index);
				
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
					status = new PlayerStatus(id, index);
					System.out.println("Room <" + roomid + ">, user <" + id + ">, index " + index);
					if(room.getUserNum() == 4 && room.get().getRoundStatus().getRound() == -1) {
						room.get().getRoundStatus().goNextRound(true, false);
						room.get().getRoundStatus().addHistory(room.get().getMatchString());
						room.sendBroadcast("start");
					}
					room.sendBroadcast(room.getMatchString());
				}
				
				do {
					msg = in.readLine();
					System.out.println("From room " + roomid + ": " + msg);
					if(msg.endsWith("stop")) {
						System.out.println("Info: Room <" + roomid + ">, remove user <" + id + ">");
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
				e.printStackTrace();
			} finally {
				room.setBlocked(false);
				try {
					deleteUserIfValid();
					room.sendBroadcast(room.getMatchString());
					System.out.println("Sending stop to client");
					out.println("stop");
					out.close();
					in.close();
					sock.close();
				} catch (Exception e) {}
			}
	}
}
