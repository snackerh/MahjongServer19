package com.snacker.MahjongFunction;

import java.util.ArrayList;
import java.util.logging.Logger;

public class RoundStatus {
	public final int MAX_ROUND;
	int round = -1;
	int extend = 0;
	int pot = 0;
	public Logger logger;
	
	// Each round is distinguished by different ArrayList<String>
	ArrayList<ArrayList<String>> history = new ArrayList<ArrayList<String>>();
	
	private int score[] = {30000, 30000, 30000, 30000};
	private boolean riichi[] = {false, false, false, false};
	
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
		history.add(new ArrayList<String>());
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
	
	public void addPot(int score) {
		pot += score;
	}
	
	public void resetPot() {
		pot = 0;
		resetRiichi();
	}
	
	public int getPot() {
		return pot;
	}
	
	public boolean isRiichi(int index) {
		return riichi[index];
	}

	public void setRiichi(int index, boolean riichi) {
		this.riichi[index] = riichi;
	}
	
	public void resetRiichi() {
		for(int i = 0; i < 4; i++) {
			setRiichi(i, false);
		}
	}

	public int getScore(int index) {
		return score[index];
	}

	public void setScore(int index, int score) {
		this.score[index] = score;
	}
	
	public void addScore(int index, int score) {
		this.score[index] += score;
	}
	
	public void addHistory(String entry) {
		history.get(history.size()-1).add(entry);
	}
	
	/* option: 1 = Rewind to just before 
	 * 2 = Rewind to start of this round
	 * 3 = Rewind to last of previous round */
	public void rewind(int option) {
		logger = Logger.getLogger("com.snacker.mahjongserver");
		
		ArrayList<String> currentRound = history.get(history.size()-1);
		ArrayList<String> previousRound;
		if(history.size() >= 2) {
			previousRound = history.get(history.size()-2);
		} else {
			previousRound = null;
		}
		String result = "";
		switch(option) {
		case 1:
			if(currentRound.size() > 1) {
				currentRound.remove(currentRound.size()-1);
				result = currentRound.get(currentRound.size()-1);
			} else {
				if(history.size() == 1) {
					logger.warning("no more history to rewind");
				} else {
					history.remove(history.size()-1);
					result = previousRound.get(previousRound.size()-1);
				}
			}
			break;
		case 2:
			for(int i = currentRound.size()-1; i > 0; i--) {
				currentRound.remove(i);
			}
			result = currentRound.get(0);
			break;
		case 3:
			history.remove(history.size()-1);
			result = previousRound.get(previousRound.size()-1);
			break;
		}
		
		if(result != "") {
			parseHistory(result);
		}
		//return result;
	}
	
	public void parseHistory(String history) {
		/* <round>|<extend>|<pot>|<id>|...|<score>|...|<riichi>|...*/
		String[] arr = history.split("\\|", -1);
		
		round = Integer.parseInt(arr[0]);
		extend = Integer.parseInt(arr[1]);
		pot = Integer.parseInt(arr[2]);
		// [3,4,5,6] = id
		for(int i = 0; i < 4; i++) {
			score[i] = Integer.parseInt(arr[7+i]);
			riichi[i] = Boolean.parseBoolean(arr[11+i]);
		}
	}
}
