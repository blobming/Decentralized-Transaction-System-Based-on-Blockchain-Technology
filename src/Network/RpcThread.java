package Network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class RpcThread extends Thread {
	
    private Socket socket;
    public String response;
    public String request;

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
            out.println("[   Welcome RPC Daemon    ]_FIN");
            while((input = in.readLine()) != null){
                if ("HELP".equalsIgnoreCase(input)){
                	out.println("############################################## COMMANDS ###############################################\n"
                    + "#     1) getinfo       - Gets block chain infomations.                                                #\n"
                    + "#     2) send <vac>    - Write <vac> to blockChain                                                    #\n"
                    + "#######################################################################################################\r\n");
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
