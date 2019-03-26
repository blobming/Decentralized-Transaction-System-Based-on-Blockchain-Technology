package obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.sleepycat.je.OperationStatus;

import Utilities.Utilities;
import berkeleyDb.MyBerkeleyDB;
/*
 * 迭代器已通过测试
 */
public class Blockchain implements Iterable<Block>{
	public String tip;	//tip means the last block of block chain
	private final String genesisCoinbaseData = "This is coinbase data";
	private MyBerkeleyDB db;
	public Blockchain(MyBerkeleyDB blockDB) {
		db = blockDB;
		db.open("Block");  //存储block的数据库
	}
	
	private Block newGenesisBlock(Transaction tx) {
		//FIXME 
		Block genesis = new Block("genesis",123456);
		genesis.setPreHashCode("genesis");
		return genesis;
		//  https://blog.csdn.net/tostick/article/details/80140145
	}
	public void addBlock(Block newB) {
		if(tip == null)	return;
		newB.setPreHashCode(tip);
		tip = newB.getHashCode();
		// modified by Jiaming begin reason: db.put methond has been changed to Object,Object
		//db.put(tip, Utilities.toByteArray(newB));
		db.put(tip, newB);
		// modified by Jiaming end reason: db.put methond has been changed to Object,Object
	}
	public void newBlockchain(String address) {	 
		String b = (String) db.get("0");	//check if genesis block(0) exist
		if(b == null) {
			Transaction coinTx = Transaction.newCoinbaseTx(address, genesisCoinbaseData);
			Block genesis = newGenesisBlock(coinTx);
			String genesisHash = genesis.getHashCode();
			// modified by Jiaming begin reason: db.put methond has been changed to Object,Object
			//db.put(genesisHash, Utilities.toByteArray(genesis));
			db.put(genesisHash, genesis);
			db.put("0", genesisHash);
			// modified by Jiaming begin reason: db.put methond has been changed to Object,Object
			tip = genesisHash;
		}else {
			tip = (String)db.get("0");
		}	
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

