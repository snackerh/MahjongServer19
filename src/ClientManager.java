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
		
	private void closeSocket() {
		try {
			writer.close();
			reader.close();
		} catch (Exception e) {}
	}
		
	public void run() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			
			if (Connections.getConnection() == 4) {
				System.out.println("Server: Server is full");
				writer.println("Server>Refused");
				writer.close();
				return;
			}
			
			while(true) {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				msg = reader.readLine();
				System.out.println("Messgae: " + msg);
				
				if(msg == null) {
					break;
				}
				
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
					Connections.add(writer, id, pos);
					System.out.println("Server: " + id + " entered the server");
					Connections.sendMessage("Server>" + Connections.getIdList());
					Connections.sendMessage("" + Connections.getConnection());
				} else if(split[0].equals("curr") || split[0].equals("new") ||
						  split[0].equals("near") || split[0].equals("prev") ||
						  split[0].equals("middle") || split[0].equals("end")) {
					StatusHandler.process(msg);
				} else {
					
				}
				
			}
			
		} catch (Exception e) {
			System.out.println("System: ERROR");
			e.printStackTrace();
		} finally {
			System.out.println("Server: connection terminated");
			Connections.remove(writer);
			closeSocket();
		}
	}

}
