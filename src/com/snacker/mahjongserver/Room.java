package com.snacker.mahjongserver;

import java.io.PrintWriter;
import java.util.ArrayList;

import com.snacker.MahjongFunction.Calculate;
import com.snacker.MahjongFunction.RoundStatus;

public class Room {
	private ArrayList<Client> clients;
	private String id;
	private RoundStatus roundStatus;
	private boolean mBlock = false;
	
	public Room(String arg) {
		clients = new ArrayList<Client>();
		for(int i = 0; i < 4; i++) {
			clients.add(null);
		}
		setId(arg);
		roundStatus = new RoundStatus();
	}
	
	public Room get() {
		return this;
	}
	
	public void setUser(int ind, Client client) {
		clients.set(ind, client);
	}
	
	public Client getUser(int ind) {
		return clients.get(ind);
	}
	
	public void deleteUser(int ind) {
		clients.set(ind, null);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public int getUserNum() {
		int count = 0;
		for(int i = 0; i < 4; i++) {
			if (clients.get(i) != null) { count++; }
		}
		return count;
	}

	public void setBlocked(boolean block) {
		mBlock = block;
	}

	public boolean isBlocked() {
		return mBlock;
	}
	
	public RoundStatus getRoundStatus() {
		return roundStatus;
	}

	/* every broadcast format should be: 
	 * <command>|<message>
	 */ 
	public int sendBroadcast(String cmd, String msg) {
		for(int i = 0; i < 4; i++) {
			Client client = clients.get(i);
			if (client != null) {
				PrintWriter out = client.getPrintWriter();
				out.println(cmd + "|" + msg);
			}
		}
		return 0;
	}
	
	/* Suggested cmd format
	 * tsumo|<han>|<fu>
	 * ron|<han>|<fu>|<loser>
	 * riichi
	 * chonbo|<loser>
	 * draw|<tenpai>|<tenpai>|<tenpai>|<tenpai>
	 * restore
	 * rewind|<option>
	 */
	public void parseCommand(String msg) {
		setBlocked(true);
		
		String[] arr = msg.split("\\|");
		if(arr.length < 2) {
			System.out.println("[" + this.id + "] Warning: input string format length too short");
		}
		if(getUserNum() != 4) {
			System.out.println("[" + this.id + "] Error: room is not full!");
			setBlocked(false);
			return;
		}
		
		int seat = Integer.parseInt(arr[0]);
		int han, fu;
		int score;
		int winScore = 0;
		int loseScore = 0;
		int loser; 
		
		int extend = roundStatus.getExtend();
		switch(arr[1]) {
		case "tsumo":
			han = Integer.parseInt(arr[2]);
			fu = Integer.parseInt(arr[3]);
			
			winScore = roundStatus.getPot();
			
			if(seat == roundStatus.getDealer()) {
				loseScore = Calculate.getScore(han, fu, 2) + 100*extend;
				winScore += Calculate.getScore(han, fu, 6) + 100*extend;
				for(int i = (seat + 1); i != seat; i = ((i + 1)%4)) {
					roundStatus.addScore(i, -loseScore);
				}
				roundStatus.addScore(seat, winScore);
				
				roundStatus.goNextRound(false, true);
				roundStatus.resetPot();
			} else {
				for(int i = (seat + 1); i != seat; i = ((i + 1)%4)) {
					if(i == roundStatus.getDealer())
						loseScore = Calculate.getScore(han, fu, 2) + 100*extend;
					else loseScore = Calculate.getScore(han, fu, 1) + 100*extend;
					winScore += loseScore;
					roundStatus.addScore(i, -loseScore);
				}
				roundStatus.addScore(seat, winScore);
				
				roundStatus.goNextRound(true, false);
				roundStatus.resetPot();
			}
			break;
		case "ron":
			han = Integer.parseInt(arr[2]);
			fu = Integer.parseInt(arr[3]);
			loser = Integer.parseInt(arr[4]);
			
			winScore = roundStatus.getPot();
			
			if(seat == roundStatus.getDealer()) {
				loseScore = Calculate.getScore(han, fu, 6) + 300*extend;
				winScore += loseScore;
				roundStatus.addScore(loser, -loseScore);
				roundStatus.addScore(seat, winScore);
				
				roundStatus.goNextRound(false, true);
			} else {
				loseScore = Calculate.getScore(han, fu, 4) + 300*extend;
				winScore += loseScore;
				roundStatus.addScore(loser, -loseScore);
				roundStatus.addScore(seat, winScore);
				roundStatus.goNextRound(true, false);
			}

			roundStatus.resetPot();
			break;
		case "riichi":
			roundStatus.addScore(seat, -1000);
			roundStatus.addPot(1000);
			roundStatus.setRiichi(seat, true);
			break;
		case "chonbo":
			loser = Integer.parseInt(arr[2]); 
			if(seat == roundStatus.getDealer()) {
				score = Calculate.getScore(5, 0, 2);
				for(int i = (seat + 1); i != seat; i = ((i + 1)%4)) {
					roundStatus.addScore(i, score);
					roundStatus.addScore(seat, -score);
				}
				roundStatus.goNextRound(false, false);
			} else {
				for(int i = (seat + 1); i != seat; i = ((i + 1)%4)) {
					if(i == roundStatus.getDealer())
						score = Calculate.getScore(5, 0, 2);
					else score = Calculate.getScore(5, 0, 1);
					roundStatus.addScore(i, score);
					roundStatus.addScore(seat, -score);
				}
				roundStatus.goNextRound(false, false);
			}
			
			for(int i = 0; i < 4; i++) {
				if(roundStatus.isRiichi(i)) {
					roundStatus.addScore(i, 1000);
					roundStatus.addPot(-1000);
					roundStatus.setRiichi(i, false);
				}
			}
			
			break;
		case "draw":
			// tenpai = 1, no-ten = 0, starting from fixed seat 0
			int tenpaiNum = 0;
			
			for (int i = 2; i < 6; i++) {
				tenpaiNum += Integer.parseInt(arr[i]);
			}
			winScore = Calculate.getDrawScore(tenpaiNum);
			loseScore = Calculate.getDrawScore(4 - tenpaiNum); 
			
			for (int i = 0; i < 4; i++) {
				if(Integer.parseInt(arr[i + 2]) == 1) {
					roundStatus.addScore(i, winScore);
				} else {
					roundStatus.addScore(i, -loseScore);
				}
			}
			if (Integer.parseInt(arr[roundStatus.getDealer()]) == 1) {
				roundStatus.goNextRound(false, true);
			} else {
				roundStatus.goNextRound(true, true);
			}
			roundStatus.resetRiichi();
			break;
		case "restore":
			// TODO: send roundstatus to requester, we need a format that client should parse it
			break;
		case "rewind":
			int option = Integer.parseInt(arr[2]);
			String result = roundStatus.rewind(option);
			break;
		default:
			System.out.println("Unknown command");
			break;
		}

		switch(arr[1]) {
			case "tsumo":
			case "ron":
			case "riichi":
			case "draw":
			case "chonbo":
				roundStatus.addHistory(getMatchString());
				break;
			default: // do nothing;
						
		}
		
		System.out.println("[" + this.id + "] " + getMatchString());
		sendBroadcast("status", getMatchString());
		if(roundStatus.getRound() == roundStatus.MAX_ROUND) {
			sendBroadcast("command", "finish");
			// TODO: save result to log
		}
		setBlocked(false);
	}

	public String getMatchString() {
	/* <round>|<extend>|<pot>|<id>|...|<score>|...|<riichi>|...*/
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(roundStatus.getRound() + "|");
		stringBuilder.append(roundStatus.getExtend() + "|");
		stringBuilder.append(roundStatus.getPot() + "|");
		
		for (int i = 0; i < 4; i++) {
			if(clients.get(i) != null) {
				stringBuilder.append(clients.get(i).getStatus().getId() + "|");
			} else {
				stringBuilder.append("|");
			}
		}
		
		for (int i = 0; i < 4; i++) {
			stringBuilder.append(roundStatus.getScore(i) + "|");
		}
		for (int i = 0; i < 4; i++) {
			stringBuilder.append(roundStatus.isRiichi(i) + "|");
		}
		
		return stringBuilder.toString();
	}
}
