package TestTestMain;
import java.io.IOException;

import obj.UTXOSet;
import userInterface.BlockChainMainThread;

public class TestMain {
	private static String networkCard = "eth0";
	public static void main(String[] args) throws NumberFormatException, IOException, InterruptedException {
		BlockChainMainThread blockChainMainThread = new BlockChainMainThread(networkCard);
		blockChainMainThread.start();
		
		System.err.println("begin send broadcast");
		//建立socket连接后，给大家广播握手
		blockChainMainThread.peerNetwork.broadcast("HEIGHT "+ UTXOSet.blockchain.getHeight());
		
	}

}
