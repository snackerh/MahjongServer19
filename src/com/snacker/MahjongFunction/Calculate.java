package com.snacker.MahjongFunction;

import java.lang.Math;

public class Calculate {
	
	public static int getScore(int han, int fu, int modifier) {
		return getScore(han, fu, modifier, 0);
	}
	
	public static int getScore(int han, int fu, int modifier, int extra) {
		int score = 0;
		switch (han) {
		case Constants.MANGAN:			score = 2000 * modifier; break;
		case Constants.HANEMAN: 		score = 3000 * modifier; break;
		case Constants.BAIMAN: 			score = 4000 * modifier; break;
		case Constants.SANBAIMAN: 		score = 6000 * modifier; break;
		case Constants.YAKUMAN: 		score = 8000 * modifier; break;
		case Constants.DOUBLE_YAKUMAN: 	score = 16000 * modifier; break;
		case Constants.TRIPLE_YAKUMAN: 	score = 24000 * modifier; break;
		case Constants.QUAD_YAKUMAN: 	score = 32000 * modifier; break;
		case Constants.QUIN_YAKUMAN: 	score = 40000 * modifier; break;
		case Constants.SEX_YAKUMAN: 	score = 48000 * modifier; break;
		default:
			score = ((int) Math.ceil((fu * Math.pow(2, han + 2) * modifier) / 100)) * 100;
			if (score > 2000 * modifier) {
				score = 2000 * modifier;
			}			
		}
		return score + extra;
	}

	public static int getDrawScore(int number) {
		if(number <= 0 || number >= 4) return 0;
		else return 3000 / number;
	}
	
	public static Pair getMinRequired(int diff, int how, int modifier, int pot, int extend) {
		// TODO: take account of the seat index in the case of same score
		Pair pair = new Pair();
		
		if(diff <= 0) {
			pair.set(0, 0);
		} else {
			switch(how) {
				case Constants.HOW_TSUMO_CHILD:
					for(int j = 20; j <= 110; j=j+10) { 
						if(2* getScore(1, j, 1, 100*extend) + getScore(1, j, 2, 100*extend) + pot >= 
								diff - getScore(1, j, modifier, 100*extend)) {
							if(j % 20 == 0) {
								pair.set(2, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}
					for(int j = 60; j <= 110; j=j+10) {
						if(2 * getScore(2, j, 1, 100*extend) + getScore(2, j, 2, 100*extend) + pot >=
								diff - getScore(2, j, modifier, 100*extend)) {
							if(j % 20 == 0) {
								pair.set(3, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}
					if(2 * getScore(3, 60, 1, 100*extend) + getScore(3, 60, 2, 100*extend) + pot >=
							diff - getScore(3, 60, modifier, 100*extend)) {
						pair.set(4, 30); // 3 han 60 fu == (3+1) han (60/2) fu
						return pair;
					}
					for(int i = Constants.MANGAN; i <= Constants.SEX_YAKUMAN; i++) {
						if(getScore(i, 0, 4, pot + 300*extend) >=
								diff - getScore(i, 0, modifier, 100*extend)) {
							pair.set(i, 0);
							return pair;
						}
					}
					break;
				case Constants.HOW_TSUMO_DEALER:
					for(int j = 20; j <= 110; j=j+10) { 
						if(4 * getScore(1, j, 2, 100*extend) + pot >= diff) {
							if(j % 20 == 0) {
								pair.set(2, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}	
					for(int j = 60; j <= 110; j=j+10) {
						if(4 * getScore(2, j, 2, 100*extend) + pot >= diff) {
							if(j % 20 == 0) {
								pair.set(3, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}					
					if(4 * getScore(3, 60, 2, 100*extend) + pot >= diff) {
						pair.set(4, 30); // 3 han 60 fu == (3+1) han (60/2) fu
						return pair;
					}
					for(int i = Constants.MANGAN; i <= Constants.SEX_YAKUMAN; i++) {
						if(4 * getScore(i, 0, 2, 100*extend) + pot >= diff) {
							pair.set(i, 0);
							return pair;
						}
					}
					break;
				case Constants.HOW_RON_CHILD:
					for(int j = 20; j <= 110; j=j+10) { 
						if(2 * getScore(1, j, 4, 300*extend) + pot >= diff) {
							if(j % 20 == 0) {
								pair.set(2, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}	
					for(int j = 60; j <= 110; j=j+10) {
						if(2 * getScore(2, j, 4, 300*extend) + pot >= diff) {
							if(j % 20 == 0) {
								pair.set(3, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}					
					if(2 * getScore(3, 60, 4, 300*extend) + pot >= diff) {
						pair.set(4, 30); // 3 han 60 fu == (3+1) han (60/2) fu
						return pair;
					}
					for(int i = Constants.MANGAN; i <= Constants.SEX_YAKUMAN; i++) {
						if(2 * getScore(i, 0, 4, 300*extend) + pot >= diff) {
							pair.set(i, 0);
							return pair;
						}
					}
					break;
				case Constants.HOW_RON_DEALER:
					for(int j = 20; j <= 110; j=j+10) { 
						if(2 * getScore(1, j, 6, 300*extend) + pot >= diff) {
							if(j % 20 == 0) {
								pair.set(2, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}	
					for(int j = 60; j <= 110; j=j+10) {
						if(2 * getScore(2, j, 6, 300*extend) + pot >= diff) {
							if(j % 20 == 0) {
								pair.set(3, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}					
					if(2 * getScore(3, 60, 6, 300*extend) + pot  >= diff) {
						pair.set(4, 30); // 3 han 60 fu == (3+1) han (60/2) fu
						return pair;
					}
					for(int i = Constants.MANGAN; i <= Constants.SEX_YAKUMAN; i++) {
						if(2 * getScore(i, 0, 6, 300*extend) + pot >= diff) {
							pair.set(i, 0);
							return pair;
						}
					}
					break;					
			}			
		}
		

		return pair;
	}
	
}
