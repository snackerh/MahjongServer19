import java.io.PrintWriter;
import java.util.ArrayList;

public class Connections {
	private static int connection;
	private static ArrayList<PrintWriter> clientList = new ArrayList<PrintWriter>();
	private static ArrayList<String> idList = new ArrayList<String>();
	
	public static void initialize() {
		connection = 0;
		clientList = new ArrayList<PrintWriter>();
		idList = new ArrayList<String>();
		
		for(int i = 0; i < 4; i++) {
			clientList.add(null);
			idList.add(null);
		}
	}
	
	public static void add(PrintWriter writer, String id, int position) {
		clientList.set(position, writer);
		idList.set(position, id);
		connection++;
		
		System.out.println("Server: connected clients=" + connection);
		
		if(connection == 4) {
			StatusHandler.initialize();
		}
	}
	
	public static String remove(PrintWriter writer) {
		String ret = null;
		int index = clientList.indexOf(writer);
		if(index != -1) {
			ret = idList.get(index);
			clientList.set(index, null);
			idList.set(index, null);
			connection--;
		}
		System.out.println("Server: connected clients=" + connection);
		return ret;
	}
	
	public static String getIdList() {
		return idList.get(0) + "#%" + idList.get(1) + "#%" + idList.get(2) + "#%" + idList.get(3); 
	}
	
	public static void sendMessageAll(String msg) {
		for(int i = 0; i < 4; i++) {
			if(clientList.get(i) != null) {
				clientList.get(i).println(msg);
			}
		}
	}
	
	public static void sendMessageExcept (String msg, int position) {
		for(int i = 0; i < 4; i++) {
			if(i == position) continue;
			else if (clientList.get(i) != null) {
				clientList.get(i).println(idList.get(position) + ">" + msg);
			}
		}
	}
	
	public static void sendMessage(String msg, int position) {
		if(clientList.get(position) != null) {
			clientList.get(position).println(msg);
		}
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
	
}
