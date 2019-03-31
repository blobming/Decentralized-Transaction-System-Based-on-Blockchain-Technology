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
		KeyValuePairs kv = new KeyValuePairs();
		Transaction coinbase = Transaction.newCoinbaseTx(kv.getPublicKey());
		BlockBody blockBody = new BlockBody(coinbase);
		Block genesis = new Block(blockBody, 123456);
		genesis.setPreHashCode("genesis");
		return genesis;
	}
	public void addBlock(Block newB) {
		//链高度++
		if(tip == null)	return;
		newB.setPreHashCode(tip);
		tip = newB.getHashCode();
		Global.blockDB.put(tip, Utilities.toByteArray(newB));
		height++;
		Global.blockDB.put("Height", height);
	}
	public void newBlockchain() {	 
		String b = (String) Global.blockDB.get("0");	//check if genesis block(0) exist
		if(b == null) {
			Block genesis = newGenesisBlock();
			String genesisHash = genesis.getHashCode();
			System.out.println(genesis+"\t"+genesisHash);
			Global.blockDB.put(genesisHash, genesis);
			Global.blockDB.put("0", genesisHash);
			tip = genesisHash;
		}else {
			tip = (String)Global.blockDB.get("0");
		}	
		height++;
		Global.blockDB.put("Height", height);
	}
	/*
	 * for every block:
	 *  for every transaction:
	 *      For Vin:
	 *          if vin.transactionId in UTXO -->remove
	 *          add into spentTXOs <vin.transactionId, vin.voutNum>  
	 *      For Vout:
	 *          if (thisTransaction.Id in spentTXOs && vout is in spentTXOs.get(thisTransactionId)):	//其实可以不要这段代码
	 *          	remove from UTXO
	 *          else:
	 *          	add into UTXO
	 */
	public static HashMap<String,HashSet<Vout>> FindUTXO(Iterator<Block> iterator){
		HashMap<String,HashSet<Vout>> UTXO = new HashMap<>();
		HashMap<String,HashSet<Integer>> spentTXOs = new HashMap<>();
		while(iterator.hasNext()) {
			Block block = (Block) iterator.next();
			ArrayList<Transaction> txs = block.getBlockBody().transactions;
			
			for(Transaction tx : txs) {
				String txID = tx.getTxid();
				Vin[] vins = tx.getVin();
				for(int i = 0; i < vins.length; i++) {
					HashSet<Vout> temp = UTXO.get(vins[i].getTxid());
					if(temp != null) {
						for(Vout vout : temp) {
							if(vout.getSeqNum() == i) {
								temp.remove(vout);
								break;
							}
						}
					}
					if(!spentTXOs.containsKey(txID)) {
						spentTXOs.put(txID, new HashSet<>());
					}
					spentTXOs.get(txID).add(vins[i].getVoutNum());
				}
				Vout[] vouts = tx.getVout();
				VoutLoop:for(int i = 0; i < vouts.length; i++) {
					if(!spentTXOs.containsKey(txID)) {
						if(UTXO.get(txID) == null) {
							UTXO.put(txID, new HashSet<Vout>());
						}
						UTXO.get(txID).add(vouts[i]);
					}else {
						for(int index : spentTXOs.get(txID)) {
							if(index == vouts[i].getSeqNum()) {
								UTXO.get(txID).remove(vouts[i]);
								continue VoutLoop;
							}
						}
						UTXO.get(txID).add(vouts[i]);
					}
				}
			}	
		}
		return UTXO;
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

