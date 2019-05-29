package Network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class PeerReader extends Thread {
    private Socket socket;
    private ArrayList<String> receivedData = new ArrayList<String>();

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
        }
    }
    
    public List<String> readData() {
        ArrayList<String> inputBuffer = new ArrayList<String>(receivedData);
        receivedData.clear(); //clear 'buffer'
        return inputBuffer;
    }
}
