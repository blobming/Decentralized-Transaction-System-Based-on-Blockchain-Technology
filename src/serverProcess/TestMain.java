package serverProcess;
import java.io.IOException;

import database.SQLDB;
import obj.UTXOSet;
import serverProcess.BlockChainMainThread;

public class TestMain {
	private static String networkCard = "eth0";
	public static void main(String[] args) throws NumberFormatException, IOException, InterruptedException {
		SQLDB.connSqlDB();
		BlockChainMainThread blockChainMainThread = new BlockChainMainThread(networkCard);
		blockChainMainThread.start();
		
		System.err.println("begin send broadcast");
		//建立socket连接后，给大家广播握手
		blockChainMainThread.peerNetwork.broadcast("HEIGHT "+ UTXOSet.blockchain.getHeight());
	}

}
