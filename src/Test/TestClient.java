package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.PrintWriter;


class RpcReader extends Thread {
    private Socket socket;

    /**
     * 传入套接字
     * @param socket
     */
    public RpcReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
    	while(true) {
	        try {
	            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            String input;
	            while ((input = in.readLine()) != null) {
	                System.out.println(input);
	            }
	            System.out.println("");
	        } catch (Exception e) {
	        	System.err.println("Peer " + socket.getInetAddress() + " disconnected.");
	        	//e.printStackTrace();
	        }
    	}
    }
}

class RpcWriter extends Thread {

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
public class TestClient {
	public static void main(String[] args) throws UnknownHostException, SocketException {
		// TODO Auto-generated method stub
		try {
	         Socket s = new Socket("127.0.0.1",8016);
	         RpcWriter rpcWriter = new RpcWriter(s);
	         RpcReader rpcReader = new RpcReader(s);
	         rpcWriter.start();
	         rpcReader.start();
	      } catch (UnknownHostException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	}
}