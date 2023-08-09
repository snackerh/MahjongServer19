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
			if (score > 8000) {
				score = 8000;
			}			
		}
		return score;
	}
	
}
