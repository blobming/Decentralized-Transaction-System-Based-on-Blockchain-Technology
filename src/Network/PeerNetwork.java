package Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PeerNetwork extends Thread {
	
    private int port;
    
    public List<PeerThread> peerThreads;
    public List<String> peers;
    
    public PeerNetwork(int port) {
    	this.port = port;
    	this.peerThreads = new ArrayList<PeerThread>();
    	this.peers = new ArrayList<String>();
    }

    public void connect(String host, int port){
    	Socket socket =null;
    	try {
    		socket = new Socket();
    		System.out.println(host);
    		socket.connect(new InetSocketAddress(host,port),5000);
			String remoteHost = socket.getInetAddress().getHostAddress();
			int remotePort = socket.getPort();
			System.out.println("socket " + remoteHost + ":" + remotePort + " connected.");
			peers.add(remoteHost + ":" + remotePort);
			PeerThread pt = new PeerThread(socket);
			peerThreads.add(pt);
			pt.start();
			System.out.println("调用join");
			pt.join();
			System.out.println("join调用结束");
		} catch (IOException | InterruptedException e) {
			System.err.println("socket " + host +":"+port+ " can't connect.");
		}
    }

    @Override
    public void run() {
    	ServerSocket listenSocket = null;
        try {
            listenSocket = new ServerSocket(port);
            while (true)
            {
            	Socket connectedSocket = listenSocket.accept();
            	peers.add(connectedSocket.getInetAddress().getHostAddress()+":"+connectedSocket.getPort());
            	PeerThread peerThread = new PeerThread(connectedSocket);
                peerThreads.add(peerThread);
                peerThread.start();
            }
        } catch (Exception e) {
           System.err.println("Listen Socket Error!");
        } finally{
        	try {
				listenSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public void broadcast(String data) {
        for (PeerThread pt: peerThreads) {
        	System.out.println("Sent:: " + data);
            if( pt!=null){
            	pt.send(data);
            }
        }
    }
}