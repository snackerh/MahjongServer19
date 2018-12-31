import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientManager extends Thread {

	Socket socket;
	PrintWriter writer;
	BufferedReader reader;
	
	String msg;
	
	String id;
	int pos;
	
	public ClientManager(Socket s) {
		socket = s;
	}
	
	private void sendMessage(String msg) {
		
	}
	
	private void closeSocket() {
		try {
			writer.close();
			reader.close();
		} catch (Exception e) {}
	}
	
	public void run() {
		try {
			writer = new PrintWriter(socket.getOutputStream());
			
			if (Connections.getConnection() == 4) {
				System.out.println("Server: Server is full");
				writer.println("Server>Refused");
				writer.close();
				return;
			}
			
			while(true) {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				msg = reader.readLine();
				
				String[] split = msg.split("####");
				
				if(split[0].equals("pos")) {
					pos = Integer.parseInt(split[1]);
					
					if(Connections.isPositionTaken(pos)) {
						System.out.println("Server: position is taken");
						writer.println("Server>posTaken");
						closeSocket();
						return;
					}
				} else if(split[0].equals("ID")) {
					id = split[1];
					
					if(Connections.containsId(id)) {
						System.out.println("Server: " + id + " already exists");
						writer.println("Server>idTaken");
						closeSocket();
						return;
					}
				} 
			}
			
		} catch (Exception e) {
			
		}
	}

}
