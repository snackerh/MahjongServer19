package com.snacker.mahjongserver;

import java.util.ArrayList;

public class RoomManager {
	ArrayList<Room> list;
	
	public RoomManager() {
		list = new ArrayList<Room>();
	}
	
	public Room findRoom(String id) {
		for (int i = 0; i < list.size(); i++) {
			Room room = list.get(i);
			if(room.getId().equals(id)) {
				return room; 
			}
		}
		return null;
	}
	
	public int addRoom(Room room) {
		list.add(room);
		return 0;
	}
	
	public int deleteRoom(Room room) {
		list.remove(room);
		return 0;
	}
}
