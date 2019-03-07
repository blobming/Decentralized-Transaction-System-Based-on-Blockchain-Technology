package Network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class PeerReader extends Thread {
    private Socket socket;

    /**缓冲区*/
    private ArrayList<String> receivedData = new ArrayList<String>();

    /**
     * 传入套接字
     * @param socket
     */
    public PeerReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;
            while ((input = in.readLine()) != null) {
                receivedData.add(input);
            }
        } catch (Exception e) {
        	System.err.println("Peer " + socket.getInetAddress() + " disconnected.");
        	//e.printStackTrace();
        }
    }

    /**
     * 取出缓冲数据
     * @return List<String> Data pulled from receivedData
     */
    public List<String> readData() {
        ArrayList<String> inputBuffer = new ArrayList<String>(receivedData);
        receivedData.clear(); //clear 'buffer'
        return inputBuffer;
    }
}
