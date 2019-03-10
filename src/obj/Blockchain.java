package obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sleepycat.je.OperationStatus;

import Utilities.Utilities;
import berkeleyDb.MyBerkeleyDB;
/*
 * 迭代器已通过测试
 */
public class Blockchain implements Iterable{
	public String tip;	//tip means the last block of blockchain
	private final String genesisCoinbaseData = "This is coinbase data";
	private MyBerkeleyDB db;
	public Blockchain() {
		db = MyBerkeleyDB.GetInstance();
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
		db.put(tip, Utilities.toByteArray(newB));
	}
	public void newBlockchain(String address) {	 
		String b = (String) db.get("0");	//check if genesis block(0) exist
		if(b == null) {
			Transaction coinTx = Transaction.newCoinbaseTx(address, genesisCoinbaseData);
			Block genesis = newGenesisBlock(coinTx);
			String genesisHash = genesis.getHashCode();
			db.put(genesisHash, Utilities.toByteArray(genesis));
			db.put("0", genesisHash);
			tip = genesisHash;
		}else {
			tip = (String)db.get("0");
		}	
	}
	/*
	public ArrayList<String> FindUTXO(){
		HashMap<String,Vout> UTXO = new HashMap<>();
		HashMap<String,ArrayList<Integer>> spentTXOs = new HashMap<>();
		Iterator iterator = this.iterator();
		while(iterator.hasNext()) {
			Block block = (Block) iterator.next();
			ArrayList<Transaction> txs = block.getBlockBody().transactions;
			
			for(Transaction tx : txs) {
				String txID = tx.getTxid();
				Outputs: 
				for(int i=0; i < tx.getVout().length; i++) {
					if(spentTXOs.get(txID) != null) {
						for(int spentOutIdx : spentTXOs.get(txID)) {
							if(spentOutIdx == i)	continue Outputs;
						}
					}
					
				}
			}
			
		}
	}
*/
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
		
//		chain.newBlockchain();
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

