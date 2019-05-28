package Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * p2p网络，负责处理peer之间的连接和通讯。每次起独立线程来处理
 * 与每一个peer的连接均新建线程保存在peerThreads里面，最终的
 * @author Mignet
 */
public class PeerNetwork extends Thread {
	
    private int port;
    private boolean runFlag = true;
    
    public List<PeerThread> peerThreads;
    public List<String> peers;

    /**
     * 默认配置
     */
    public PeerNetwork() {
        this.port = 8015;
        this.peerThreads = new ArrayList<PeerThread>();
        this.peers = new ArrayList<String>();
    }
    /**
     * 传绑定端口
     * @param port
     */
    public PeerNetwork(int port) {
    	this.port = port;
    	this.peerThreads = new ArrayList<PeerThread>();
    	this.peers = new ArrayList<String>();
    }

    /**
     * 建立连接
     *
     * @param 要连接的host
     * @param 要连接的host的端口
     */
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
        try {
            ServerSocket listenSocket = new ServerSocket(port);
            while (runFlag) 
            {
            	System.out.println("dedaole lianjie");
            	Socket connectedSocket = listenSocket.accept();
            	System.out.println("writing ip to network peers");
            	peers.add(connectedSocket.getInetAddress().getHostAddress()+":8015");
            	PeerThread peerThread = new PeerThread(connectedSocket);
                peerThreads.add(peerThread);
                peerThread.start();
            }
            listenSocket.close();
        } catch (Exception e) {
           System.err.println("{}");
        }
    }

    /**
     * 广播消息
     * @param data String to broadcast to peers
     */
    public void broadcast(String data) {
        for (PeerThread pt: peerThreads) {
        	System.out.println("Sent:: " + data);
            if( pt!=null){
            	pt.send(data);
            }
        }
    }
}