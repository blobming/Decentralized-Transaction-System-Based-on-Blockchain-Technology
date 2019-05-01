package obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.sleepycat.je.OperationStatus;

import Security.KeyValuePairs;
import Utilities.Utilities;
import berkeleyDb.MyBerkeleyDB;
import config.Global;
/*
 * 迭代器已通过测试
 */
public class Blockchain implements Iterable<Block>{
	public String tip;	//tip means the last block of block chain
	private final String genesisCoinbaseData = "This is coinbase data";
	private int height;
	public Blockchain() {
		//从configdb里取出链高度
		height = Global.blockDB.get("Height") == null ? 0 : (int)Global.blockDB.get("Height");
	}
	
	public int getHeight() {
		return height;
	}

	private Block newGenesisBlock() {
		Transaction coinbase = Transaction.genesisCoinbaseTx(Global.keyValuePairs.getPublicKey());
		Global.genesisTX = coinbase.getHash();
		BlockBody blockBody = new BlockBody(coinbase);
		Block genesis = new Block(blockBody, 123456);
		genesis.setPreHashCode("genesis");
		return genesis;
	}
	public boolean addBlock(Block newB) {
		if(tip == null)	return false;
		if(getBlock(newB.getHashCode()) != null)	return false;
		newB.setPreHashCode(tip);
		tip = newB.getHashCode();
		Global.blockDB.put(tip, newB);
		height++;
		Global.blockDB.put("Height", height);
		return true;
	}
	
	//not tested
	public Block getBlock(String hashcode) {
		Object result = (Block) Global.blockDB.get(Utilities.toByteArray(hashcode));
		if(result == null)	return null;
		else {
			return (Block)result;
		}
	}
	
	public void newBlockchain() {	 
		String b = (String) Global.blockDB.get("0");	//check if genesis block(0) exist
		if(b == null) {
			System.out.println("add into db");
			Block genesis = newGenesisBlock();
			String genesisHash = genesis.getHashCode();
			System.out.println(genesis+"\t"+genesisHash);
			Global.blockDB.put(genesisHash, genesis);
			System.out.println("hash:" + genesisHash);
			Global.blockDB.put("0", genesisHash); 
			tip = genesisHash;
		}else {
			System.out.println("has sth in blochchain!");
			tip = (String)Global.blockDB.get("0");
			System.out.println("first block:" + tip);
		}	
		height++;
		Global.blockDB.put("Height", height);
	}
	
	@Override
	public Iterator<Block> iterator() {
		return new BlockchainIterator();
	}
	
	private class BlockchainIterator implements Iterator<Block>{
		String point = tip;
		@Override
		public boolean hasNext() {	
			if(Global.blockDB.get(point)== null)
				return false;
			else return true;
		}

		@Override
		public Block next() {
			Block ret = (Block)Global.blockDB.get(point);
			point = ret.getPreHashCode();
			return ret;
		}
	}
	
	public static void writeBlock(ArrayList<Block> blockList) {
		for(Block block: blockList) {
			Global.blockDB.put(block.getHashCode(), Utilities.toByteArray(block));
		}
	}

	//for test
	public static void main(String[] args) {
//		Blockchain chain = new Blockchain();
//		
////		chain.newBlockchain();
//		Block one = new Block("one",234);
//		Block two = new Block("two",345);
//		Block three = new Block("three",567);
//		chain.addBlock(one);
//		chain.addBlock(two);
//		chain.addBlock(three);
//		Iterator<Block> iterator = chain.iterator();
//		while(iterator.hasNext()) {
//			Block b = (Block) iterator.next();
//			System.out.println(b.getMerkleRootHash());
//		}
//		
	}
}

