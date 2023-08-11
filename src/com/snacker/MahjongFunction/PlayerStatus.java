package com.snacker.MahjongFunction;

public class PlayerStatus {
	private int seatIndex;
	private String id;
	
	public PlayerStatus() {
	}
	
	public PlayerStatus(String id, int index) {
		setId(id);
		setSeatIndex(index);
	}

	public int getSeatIndex() {
		return seatIndex;
	}

	public void setSeatIndex(int seatIndex) {
		this.seatIndex = seatIndex;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
