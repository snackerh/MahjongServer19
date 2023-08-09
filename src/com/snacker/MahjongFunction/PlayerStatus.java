package com.snacker.MahjongFunction;

public class PlayerStatus {
	private int seatIndex;
	private int score;
	
	public PlayerStatus() {
		setScore(30000);
	}
	
	public PlayerStatus(int index) {
		setSeatIndex(index);
		setScore(30000);
	}

	public int getSeatIndex() {
		return seatIndex;
	}

	public void setSeatIndex(int seatIndex) {
		this.seatIndex = seatIndex;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
}
