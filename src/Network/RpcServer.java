package Network;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * RPC服务
 *
 * 注意：不要把这个端口开放给外网
 * @author Mignet
 */
public class RpcServer extends Thread
{
    private int port;
    private boolean runFlag = true;

    public List<RpcThread> rpcThreads;

    /**
     * 默认配置
     */
    public RpcServer()
    {
        this.port = 8016;
        this.rpcThreads = new ArrayList<RpcThread>();
    }

    /**
     * 指定端口
     * @param port Port to listen on
     */
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
