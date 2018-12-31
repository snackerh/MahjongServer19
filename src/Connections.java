import java.util.ArrayList;

public class Connections {
	private static int connection;
	private static ArrayList<ClientManager> clientList = new ArrayList<ClientManager>();
	private static ArrayList<String> idList = new ArrayList<String>();
	
	public static void initialize() {
		connection = 0;
		clientList = new ArrayList<ClientManager>();
		idList = new ArrayList<String>();
	}
	
	public static int getConnection() {
		return connection;
	}
	
	public static boolean isPositionTaken(int position) {
		return (idList.get(position) == null ? false : true);
	}
	
	public static boolean containsId(String id) {
		return (idList.contains(id) ? true : false);
	}
	
	public static void set(ClientManager client) {
		
	}
}
