import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	static ServerSocket serversocket;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connections.initialize();
		runServer();
	}

	private static void runServer() {
		boolean running = true;
		System.out.println("Server: booting up server, port 9004");
		try {
			serversocket = new ServerSocket(9004);
			
			while(running) {
				System.out.println("Server: waiting for new connection");
				
				Socket socket = serversocket.accept();
				ClientManager client = new ClientManager(socket);
				
				client.start();
				System.out.println("Server: new connection established");
			}
		} catch (Exception e) {
			System.out.println("System: ERROR");
			e.printStackTrace();
		} finally {
			try {
				serversocket.close();
			} catch (Exception e) {}
		}
	}
	
}
