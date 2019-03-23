package Network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * 处理单个rpc连接
 * @author Mignet
 */
public class RpcThread extends Thread {
	
    private Socket socket;
    public String response;
    public String request;

    /**
     * 默认构造函数
     * @param socket
     */
    public RpcThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            request = null;
            response = null;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;
            out.println("[   Welcome RPC Daemon    ]");
            while((input = in.readLine()) != null){
                if ("HELP".equalsIgnoreCase(input)){
                	out.println("############################################## COMMANDS ###############################################");
                    out.println("#     1) getinfo       - Gets block chain infomations.                                                #");
                    out.println("#     2) send <vac>    - Write <vac> to blockChain                                                    #");
                    out.println("#######################################################################################################");
                } else {
                    request = input;
                    while (response == null){
                    	TimeUnit.MILLISECONDS.sleep(25);
                    }
                    out.println(response);
                    request = null;
                    response = null;
                }
            }
        } catch (Exception e){
            System.err.println(("An RPC client has disconnected."));
        }
    }
}
