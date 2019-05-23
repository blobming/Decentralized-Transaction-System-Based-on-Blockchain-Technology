package obj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.sleepycat.je.OperationStatus;

import Security.KeyValuePairs;
import database.MyBerkeleyDB;
import utilities.Utilities;
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
		System.out.println("Global.GenesisTX+ "+coinbase.getHash());
		BlockBody blockBody = new BlockBody(coinbase);
		Date timeStamp = new Date();
		try {
			timeStamp = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Block genesis = Blockchain.MiningBlock(new Block(blockBody, 0, "genesis",timeStamp));
		return genesis;
	}
	//仅限于从其他节点同步块时调用
	public boolean addBlock(Block newB) {
		if(getBlock(newB.getHashCode()) != null) {
			System.err.println("Block with HASH["+newB.getHashCode()+"] already exists");
			return false;
		}
		if(!tip.equals(newB.getPreHashCode())) {
			System.err.println("Block with HASH["+newB.getHashCode()+"]'s preHash unsatisfied");
			return false;
		}
		for(Transaction transaction:newB.getBlockBody().transactions) {
			if(!Transaction.validateTransaction(transaction, newB)) {
				System.err.println("Transaction with HASH["+transaction.getHash()+"] validated fail");
				return false;
			}
		}
		if(!newB.getHashCode().equals(newB.calculateHash())) {
			System.err.println("Block with HASH["+newB.getHashCode()+"]'s HashCode Wrong");
			return false;
		}
		tip = newB.getHashCode();
		Global.blockDB.put(tip, newB);
		Global.blockDB.put("0", tip);
		UTXOSet.Update(newB.getBlockBody());
		for(Transaction t : newB.getBlockBody().transactions) {
			if(TXPool.contains(t)) {
				TXPool.removeTX(t);
			}
		}
		height++;
		Global.blockDB.put("Height", height);
		return true;
	}
	
	//not tested
	public Block getBlock(String hashcode) {
		Object result = Global.blockDB.get(hashcode);
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
			height = 1;
			Global.blockDB.put("Height", height);
		}else {
			System.out.println("has sth in blochchain!");
			tip = (String)Global.blockDB.get("0");
			System.out.println("first block:" + tip);
		}
		
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
	public static String generateInv(Blockchain bc, String hashcode){
		StringBuilder sb = new StringBuilder();
		for(Block block : bc) {
			if(!block.getHashCode().equals(hashcode)) {
				sb.append(block.getHashCode() + ",");
			}else {
				break;
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	public static Block MiningBlock(Block block) {
		String zeroNum = "";
		for(int i=0;i<block.getnBits();i++) {
			zeroNum += "0";
		}
		while(!block.calculateHash().startsWith(zeroNum)) {
			block.setNonce(block.getNonce()+1);
		}
		block.setHashCode(block.calculateHash());
		return block;
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

