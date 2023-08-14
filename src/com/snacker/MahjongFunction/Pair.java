package com.snacker.MahjongFunction;

public class Pair {
	int han = 0;
	int fu = 0;
	
	public Pair() {
	}
	
	public Pair(int han, int fu) {
		set(han, fu);
	}
	
	public int getHan() {
		return han;
	}
	
	public int getFu() {
		return fu;
	}
	
	public void set(int han, int fu) {
		this.han = han;
		this.fu = fu;
	}
}
