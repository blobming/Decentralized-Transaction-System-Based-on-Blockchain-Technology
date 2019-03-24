package Network;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RpcWriter extends Thread {

	private Socket socket;
	private boolean runFlag = true;

	public RpcWriter(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			while (runFlag) {
				String line = new Scanner(System.in).nextLine(); 
            	System.out.println("Sending " +line + " to " + socket.getInetAddress());
                out.println(line);
            }
		} catch (Exception e) {
			 System.err.println("Peer " + socket.getInetAddress() + " disconnected."+e); 
		}
	}
}