package obj;

import java.util.Iterator;

import com.sleepycat.je.OperationStatus;

import Utilities.Utilities;
import berkeleyDb.MyBerkeleyDB;

public class Blockchain implements Iterable{
	public String tip;	//tip means the last block of blockchain
	private MyBerkeleyDB db;
	public Blockchain() {
		db = MyBerkeleyDB.GetInstance();
		db.open("Test");  //存储block的数据库
	}
	
	private Block newGenesisBlock() {
		Block genesis = new Block("genesis",123456);
		genesis.setPreHashCode("genesis");
		return genesis;
		//  https://blog.csdn.net/tostick/article/details/80140145
	}
	public void addBlock(Block newB) {
		if(tip == null)	return;
		newB.setPreHashCode(tip);
		tip = newB.getHashCode();
		db.put(tip, Utilities.toByteArray(newB));
	}
	public void newBlockchain() {	 
		String b = (String) db.get("0");	//check if genesis block(0) exist
		if(b == null) {
			Block genesis = newGenesisBlock();
			String genesisHash = genesis.getHashCode();
			db.put(genesisHash, Utilities.toByteArray(genesis));
			db.put("0", genesisHash);
			tip = genesisHash;
		}else {
			tip = (String)db.get("0");
		}	
	}

	@Override
	public Iterator iterator() {
		return new BlockchainIterator();
	}
	
	private class BlockchainIterator implements Iterator{
		String point = tip;
		@Override
		public boolean hasNext() {	
			if(db.get(point)== null)
				return false;
			else return true;
		}

		@Override
		public Block next() {
			Block ret = (Block)db.get(point);
			point = ret.getPreHashCode();
			return ret;
		}
	}
	
	//for test
	public static void main(String[] args) {
		Blockchain chain = new Blockchain();
		
		chain.newBlockchain();
		Block one = new Block("one",234);
		Block two = new Block("two",345);
		Block three = new Block("three",567);
		chain.addBlock(one);
		chain.addBlock(two);
		chain.addBlock(three);
		Iterator iterator = chain.iterator();
		while(iterator.hasNext()) {
			Block b = (Block) iterator.next();
			System.out.println(b.getMerkleRootHash());
		}
		
	}
}

