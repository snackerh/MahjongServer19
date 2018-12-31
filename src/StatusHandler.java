import java.util.ArrayList;

public class StatusHandler {
	
	private static ArrayList<String> roundStatus;
	private static ArrayList<ArrayList<String>> matchStatus;
	
	public static void initialize() {
		roundStatus = new ArrayList<String>();
		matchStatus = new ArrayList<ArrayList<String>>();
	}
	
	public static boolean isPlaying() {
		return (matchStatus.size() != 0 || roundStatus.size() != 0);
	}
	
	public static void process(String msg) {
		String[] split = msg.split("####");
		
		if(split[0].equals("new")) {
			if(!roundStatus.isEmpty()) {
				matchStatus.add(roundStatus);
				roundStatus = new ArrayList<String>();
			}
			roundStatus.add(split[1]);
		} else if(split[0].equals("curr")) {
			roundStatus.add(split[1]);
		} else if(split[0].equals("end")) {
			//matchStatus.add(roundStatus);
			//roundStatus = new ArrayList<String>();
			System.out.println("Server: match ended");
			matchStatus.clear();
			roundStatus.clear();
		} else if(msg.equals("near")) {
			if(roundStatus.size() == 1) {
				if(matchStatus.size() != 0) {
					roundStatus = matchStatus.get(matchStatus.size()-1);
					matchStatus.remove(matchStatus.size()-1);
				}
			} else {
				roundStatus.remove(roundStatus.size()-1);
			}
			Connections.sendMessage("Server>near>" + roundStatus.get(roundStatus.size()-1));
		} else if(msg.equals("middle")) {
			String temp = roundStatus.get(0);
			
			roundStatus.clear();
			roundStatus.add(temp);
			Connections.sendMessage("Server>middle>" + roundStatus.get(0));
		} else if(msg.equals("far")) {
			String temp = matchStatus.get(matchStatus.size()-1).get(0);
			
			matchStatus.remove(matchStatus.size()-1);
			roundStatus.clear();
			roundStatus.add(temp);
			Connections.sendMessage("Server>far" + roundStatus.get(0));
		} else if(split[0].equals("start")) {
			if (!isPlaying()) {
				roundStatus.add(split[1]);
			}
		}
	}
}
