package Network;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * OutputThread 负责写数据到peer
 * @author Mignet
 */
public class PeerWriter extends Thread {

	private Socket socket;
	private ArrayList<String> outputBuffer = new ArrayList<String>();

	public PeerWriter(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				if (!outputBuffer.isEmpty() && outputBuffer.get(0) != null) {
                    for (String line : outputBuffer) {
                    	System.out.println("Sending " +line + " to " + socket.getInetAddress());
                        out.println(line);
                    }
                    outputBuffer = new ArrayList<String>();
                    outputBuffer.add(null);
                }
				outputBuffer = new ArrayList<String>();
				outputBuffer.add(null);
				TimeUnit.MILLISECONDS.sleep(200);
			}
		} catch (Exception e) {
			 System.err.println("Peer " + socket.getInetAddress() + " disconnected."+e); 
		}
	}

	/**
	 * 写入缓冲
	 *
	 * @param data Data to write
	 */
	public void write(String data) {
		if (!outputBuffer.isEmpty()) {
			if (outputBuffer.get(0) == null) {
				outputBuffer.remove(0);
			}
		}
		outputBuffer.add(data);
	}
}