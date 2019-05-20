package Test;
import java.io.IOException;
import userInterface.BlockChainEntrance;

public class TestMain {
	private static String networkCard = "en0";
	public static void main(String[] args) throws NumberFormatException, IOException, InterruptedException {
		BlockChainEntrance blockChainEntrance = new BlockChainEntrance(networkCard);
		blockChainEntrance.start();
	}

}
