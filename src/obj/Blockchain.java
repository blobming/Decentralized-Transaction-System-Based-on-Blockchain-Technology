package obj;

import java.util.ArrayList;

public class Blockchain {
	public Block tip;	//tip means the last block of blockchain
	
	public Blockchain() {
		
	}
	
	public Block newGenesisBlock() {
		return null;
		//  https://blog.csdn.net/tostick/article/details/80140145
	}
	public void addBlock(Block newB) {
		newB.setPreHashCode(tip.getHashCode());
		tip = newB;
		// save into db
	}
	public void newBlockchain(Block genesisBlock) {
		
	}
	
}

