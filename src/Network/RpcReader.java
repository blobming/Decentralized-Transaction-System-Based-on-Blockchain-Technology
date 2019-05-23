//package Network;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.Socket;
//
//public class RpcReader extends Thread {
//    private Socket socket;
//
//    /**
//     * 传入套接字
//     * @param socket
//     */
//    public RpcReader(Socket socket) {
//        this.socket = socket;
//    }
//
//    @Override
//    public void run() {
//    	while(true) {
//	        try {
//	            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//	            String input;
//	            while ((input = in.readLine()) != null) {
//	                System.out.println(input);
//	            }
//	            System.out.println("");
//	        } catch (Exception e) {
//	        	System.err.println("Peer " + socket.getInetAddress() + " disconnected.");
//	        	//e.printStackTrace();
//	        }
//    	}
//    }
//}