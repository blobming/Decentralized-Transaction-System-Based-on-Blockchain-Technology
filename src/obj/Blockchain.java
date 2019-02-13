package obj;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import berkeleyDb.MyBerkeleyDB;

public class Blockchain {
	public Block tip;	//tip means the last block of blockchain
	private MyBerkeleyDB db;
	public Blockchain() {
		db = MyBerkeleyDB.GetInstance();
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
		db.open("Test");  //存储block的数据库
		try {
			Block b = (Block) db.get("0");
			if(b == null) {
				Block genesis = newGenesisBlock();
			}else {
				
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

