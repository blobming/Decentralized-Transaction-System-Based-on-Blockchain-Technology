package Network;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class RpcServer extends Thread
{
    private int port;
    private boolean runFlag = true;

    public List<RpcThread> rpcThreads;

    public RpcServer(int port)
    {
        this.port = port;
        this.rpcThreads = new ArrayList<RpcThread>();
    }

    @Override
    public void run()
    {
        try
        {
            ServerSocket socket = new ServerSocket(port);
            while (runFlag)
            {
            	RpcThread thread = new RpcThread(socket.accept());
                rpcThreads.add(thread);
                thread.start();
            }
            socket.close();
        } catch (Exception e){
        	System.err.println("rpc error in port:" + port);
        }
    }
}
