package com.snacker.mahjongserver;

import java.io.PrintWriter;
import java.util.ArrayList;

import com.snacker.MahjongFunction.Calculate;
import com.snacker.MahjongFunction.RoundStatus;

public class Room {
	private ArrayList<Client> clients;
	private String id;
	private RoundStatus roundStatus;
	
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
	
	public int sendBroadcast(String msg) {
		for(int i = 0; i < 4; i++) {
			Client client = clients.get(i);
			if (client != null) {
				PrintWriter out = client.getPrintWriter();
				out.println(msg);
			}
		}
		return 0;
	}
	
	/* Suggested cmd format
	 * <seat>::tsumo::<han>::<fu>
	 * <seat>::ron::<han>::<fu>::<loser>
	 * <seat>::chonbo
	 * <seat>::draw
	 * <seat>::restore
	 * <seat>::rewind::<option>
	 */
	public void parseCommand(String msg) {
		String[] arr = msg.split("::");
		int seat = Integer.parseInt(arr[0]);
		int han, fu;
		int score;
		int extend = roundStatus.getExtend();
		switch(arr[1]) {
		case "tsumo":
			han = Integer.parseInt(arr[2]);
			fu = Integer.parseInt(arr[3]);
			if(seat == roundStatus.getDealer()) {
				score = Calculate.getScore(han, fu, 2) + 100*extend;
				for(int i = (seat + 1); i != seat; i = ((i + 1)%4)) {
					clients.get(i).getStatus().addScore(-score);
					clients.get(seat).getStatus().addScore(score);
				}
				roundStatus.goNextRound(false, true);
			} else {
				for(int i = (seat + 1); i != seat; i = ((i + 1)%4)) {
					if(i == roundStatus.getDealer())
						score = Calculate.getScore(han, fu, 2) + 100*extend;
					else score = Calculate.getScore(han, fu, 1) + 100*extend;
					clients.get(i).getStatus().addScore(-score);
					clients.get(seat).getStatus().addScore(score);
				}
				roundStatus.goNextRound(true, false);
			}
			break;
		case "ron":
			han = Integer.parseInt(arr[2]);
			fu = Integer.parseInt(arr[3]);
			int loser = Integer.parseInt(arr[4]);
			if(seat == roundStatus.getDealer()) {
				score = Calculate.getScore(han, fu, 6) + 300*extend;
				clients.get(loser).getStatus().addScore(-score);
				clients.get(seat).getStatus().addScore(score);
				roundStatus.goNextRound(false, true);
			} else {
				score = Calculate.getScore(han, fu, 4) + 300*extend;
				clients.get(loser).getStatus().addScore(-score);
				clients.get(seat).getStatus().addScore(score);
				roundStatus.goNextRound(true, false);
			}
			break;
		case "chonbo":
			if(seat == roundStatus.getDealer()) {
				score = Calculate.getScore(5, 0, 2);
				for(int i = (seat + 1); i != seat; i = ((i + 1)%4)) {
					clients.get(i).getStatus().addScore(score);
					clients.get(seat).getStatus().addScore(-score);
				}
				roundStatus.goNextRound(false, false);
			} else {
				for(int i = (seat + 1); i != seat; i = ((i + 1)%4)) {
					if(i == roundStatus.getDealer())
						score = Calculate.getScore(5, 0, 2);
					else score = Calculate.getScore(5, 0, 1);
					clients.get(i).getStatus().addScore(score);
					clients.get(seat).getStatus().addScore(-score);
				}
				roundStatus.goNextRound(false, false);
			}
			break;
		case "draw":
			// TODO: what should we do?
			break;
		case "restore":
			// TODO: send roundstatus to requester, we need a format that client should parse it
			break;
		case "rewind":
			// TODO: we need more implementation to use this
			break;
		default:
			System.out.println("Unknown command");
			break;
		}
	}
}
