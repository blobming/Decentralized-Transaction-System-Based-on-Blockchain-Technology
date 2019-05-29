package Network;

import java.net.Socket;

public class PeerThread extends Thread
{
    private Socket socket;
    public PeerReader peerReader;
    public PeerWriter peerWriter;
    
    public PeerThread(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
    	System.out.println("begin connection from " + socket.getInetAddress() + ":" +socket.getPort() + ".");
        peerReader = new PeerReader(socket);
        peerReader.start();
        peerWriter = new PeerWriter(socket);
        peerWriter.start();
    }

    public void send(String data)
    {
        if (peerWriter == null)
        {
        	System.err.println("Couldn't send " + data + " when outputThread is null");
        }
        else
        {
            peerWriter.write(data);
        }
    }
}