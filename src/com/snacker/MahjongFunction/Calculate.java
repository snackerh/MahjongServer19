package com.snacker.MahjongFunction;

import java.lang.Math;

public class Calculate {
	
	public static int getScore(int han, int fu, int modifier) {
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
		return score;
	}

	public static int getDrawScore(int number) {
		if(number == 0 || number == 4) return 0;
		else return 3000 / number;
	}
	
	public static Pair getMinRequired(int score, int goal, int how, int modifier) {
		// TODO: take account of the seat index in the case of same score
		Pair pair = new Pair();
		
		if(score >= goal) {
			pair.set(0, 0);
		} else {
			switch(how) {
				case Constants.HOW_TSUMO_CHILD:
					for(int j = 20; j <= 110; j=j+10) { 
						if(2* getScore(1, j, 1) + getScore(1, j, 2) >= (goal - score) - getScore(1, j, modifier)) {
							if(j % 20 == 0) {
								pair.set(2, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}
					for(int j = 60; j <= 110; j=j+10) {
						if(2 * getScore(2, j, 1) + getScore(2, j, 2) >= (goal - score) - getScore(2, j, modifier)) {
							if(j % 20 == 0) {
								pair.set(3, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}
					if(2 * getScore(3, 60, 1) + getScore(3, 60, 2)  >= (goal - score) - getScore(3, 60, modifier)) {
						pair.set(4, 30); // 3 han 60 fu == (3+1) han (60/2) fu
						return pair;
					}
					for(int i = Constants.MANGAN; i <= Constants.SEX_YAKUMAN; i++) {
						if(getScore(i, 0, 4) >= goal - score - getScore(i, 0, modifier)) {
							pair.set(i, 0);
							return pair;
						}
					}
					break;
				case Constants.HOW_TSUMO_DEALER:
					for(int j = 20; j <= 110; j=j+10) { 
						if(4 * getScore(1, j, 2) >= (goal - score)) {
							if(j % 20 == 0) {
								pair.set(2, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}	
					for(int j = 60; j <= 110; j=j+10) {
						if(4 * getScore(2, j, 2) >= (goal - score)) {
							if(j % 20 == 0) {
								pair.set(3, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}					
					if(4 * getScore(3, 60, 2)  >= (goal - score)) {
						pair.set(4, 30); // 3 han 60 fu == (3+1) han (60/2) fu
						return pair;
					}
					for(int i = Constants.MANGAN; i <= Constants.SEX_YAKUMAN; i++) {
						if(4 * getScore(i, 0, 2) >= goal - score) {
							pair.set(i, 0);
							return pair;
						}
					}
					break;
				case Constants.HOW_RON_CHILD:
					for(int j = 20; j <= 110; j=j+10) { 
						if(2 * getScore(1, j, 4) >= (goal - score)) {
							if(j % 20 == 0) {
								pair.set(2, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}	
					for(int j = 60; j <= 110; j=j+10) {
						if(2 * getScore(2, j, 4) >= (goal - score)) {
							if(j % 20 == 0) {
								pair.set(3, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}					
					if(2 * getScore(3, 60, 4)  >= (goal - score)) {
						pair.set(4, 30); // 3 han 60 fu == (3+1) han (60/2) fu
						return pair;
					}
					for(int i = Constants.MANGAN; i <= Constants.SEX_YAKUMAN; i++) {
						if(2 * getScore(i, 0, 4) >= goal - score) {
							pair.set(i, 0);
							return pair;
						}
					}
					break;
				case Constants.HOW_RON_DEALER:
					for(int j = 20; j <= 110; j=j+10) { 
						if(2 * getScore(1, j, 6) >= (goal - score)) {
							if(j % 20 == 0) {
								pair.set(2, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}	
					for(int j = 60; j <= 110; j=j+10) {
						if(2 * getScore(2, j, 6) >= (goal - score)) {
							if(j % 20 == 0) {
								pair.set(3, j/2); // X han Y fu == (X+1) han (Y/2) fu
							} else {
								pair.set(1, j);
							}
							return pair;
						}
					}					
					if(2 * getScore(3, 60, 6)  >= (goal - score)) {
						pair.set(4, 30); // 3 han 60 fu == (3+1) han (60/2) fu
						return pair;
					}
					for(int i = Constants.MANGAN; i <= Constants.SEX_YAKUMAN; i++) {
						if(2 * getScore(i, 0, 6) >= goal - score) {
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
