package Test;
import java.io.IOException;

import obj.UTXOSet;
import userInterface.BlockChainEntrance;

public class TestMain {
	private static String networkCard = "en0";
	public static void main(String[] args) throws NumberFormatException, IOException, InterruptedException {
		BlockChainEntrance blockChainEntrance = new BlockChainEntrance(networkCard);
		blockChainEntrance.start();
		
		System.err.println("begin send broadcast");
		//建立socket连接后，给大家广播握手
		blockChainEntrance.peerNetwork.broadcast("HEIGHT "+ UTXOSet.blockchain.getHeight());
	}

}
