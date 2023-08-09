package com.snacker.MahjongFunction;

public class RoundStatus {
	final int MAX_ROUND;
	int round = -1;
	int extend = 0;
	
	public RoundStatus() {
		MAX_ROUND = 8;
	}
	
	public RoundStatus(int max) {
		MAX_ROUND = max;
	}
	
	public void goNextRound(boolean isRoundChange, boolean isExtend) {
		if(isRoundChange) round++;
		if(isExtend) extend++;
		else extend = 0;
	}
	
	public int getRound() {
		return round;
	}
	
	public int getDealer() {
		return round % 4;
	}
	
	public int getExtend() {
		return extend;
	}
}
