package obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import com.mysql.fabric.xmlrpc.base.Array;

import database.MyBerkeleyDB;
import utilities.Utilities;
import config.Global;

public class UTXOSet {
	public static Blockchain blockchain;
	public static ArrayList<Vout> FindUTXO(String address) {
		ArrayList<Vout> utxo = new ArrayList<Vout>();
		for(Entry<String, Object> entry : Global.utxoDB.getAllKey().entrySet()) {
			HashSet<Vout> vouts = (HashSet<Vout>) entry.getValue();
			for(Vout vout : vouts) {
				if(vout.IsLockedWithKey(Utilities.hashKeyForDisk(address))) {
					utxo.add(vout);
				}
			}
		}
		return utxo;
	}
	public static double getBalance(String address) {
		ArrayList<Vout> vouts = FindUTXO(address);
		double balance = 0;
		for(Vout vout : vouts) {
			balance += vout.getValue();
		}
		return balance;
	}
	/* previous Version
	public static HashMap<String, HashSet<Vout>> FindSpendableOutputs(String address, double amount) {
		HashMap<String, HashSet<Vout>> unspentOutputs = new HashMap<>();
		double accumulated = 0;
		for(Entry<String, Object> entry : Global.utxoDB.getAllKey().entrySet()) {
			HashSet<Vout> vouts = (HashSet<Vout>) entry.getValue();
			for(Vout vout : vouts) {
				if(vout.IsLockedWithKey(Utilities.hashKeyForDisk(address)) && accumulated < vout.getValue()) {
					accumulated += vout.getValue();
					if(!unspentOutputs.containsKey(entry.getKey())) {
						unspentOutputs.put(entry.getKey(), new HashSet<Vout>());
					}
					unspentOutputs.get(entry.getKey()).add(vout);
				}
			}
		}
		return unspentOutputs;
	}
	*/
	public static HashMap<String, HashSet<Vout>> FindSpendableOutputs(String address, double amount) {
		HashMap<String, HashSet<Vout>> unspentOutputs = new HashMap<>();
		double accumulated = 0;
		for(Entry<String, Object> entry : Global.utxoDB.getAllKey().entrySet()) {
			HashSet<Vout> vouts = (HashSet<Vout>) entry.getValue();
			for(Vout vout : vouts) {
				if(vout.IsLockedWithKey(Utilities.hashKeyForDisk(address)) && accumulated < amount) {
					accumulated += vout.getValue();
					if(!unspentOutputs.containsKey(entry.getKey())) {
						unspentOutputs.put(entry.getKey(), new HashSet<Vout>());
					}
					unspentOutputs.get(entry.getKey()).add(vout);
				}
			}
		}
		System.out.println("accumulated: "+accumulated+"\t"+"amount: "+amount);
		return unspentOutputs;
	}
	public static void Reindex() {
		Global.utxoDB.closeDatabase();
		Global.utxoDB.getEnvironment().truncateDatabase(null, "UTXO", false);
		Global.utxoDB.open("UTXO");
		HashMap<String,HashSet<Vout>> utxo = FindAllUTXO(blockchain);
		for(Entry<String, HashSet<Vout>> entry : utxo.entrySet()) {
			Global.utxoDB.put(entry.getKey(), entry.getValue());
		}
	}
	//Update updates the UTXO set with transactions from the Block
	// The Block is considered to be the tip of a blockchain
	/* if isCoinbaseTX continue;
	 *	  for each vin:
	 *	      if(vin.txid is in UTXO && vin.voutNum is in utxo[vin.txid])	remove it
	 * for each vout:
	 *        add into UTXO
	 */
	public static void Update(BlockBody blockbody) {
		ArrayList<Transaction> txs = UTXOSet.sortTransaction(blockbody.transactions);
		//ArrayList<Transaction> txs = blockbody.transactions;
		for(int i=0;i<txs.size();i++) {
			System.out.println(txs.get(i).getTimestamp());
		}
		for(Transaction tx : txs) {
			if(!tx.isCoinBase()) {
				System.out.println("not coinbase");
				Vin[] vins = tx.getVin();
				for(int i=0;i<vins.length;i++) {
					HashSet<Vout> outputs = (HashSet<Vout>) Global.utxoDB.get(vins[i].getTxid());
					HashSet<Vout> newOutputs = new HashSet<>();
					System.out.println("old:" + outputs.size());
					for(Vout vout : outputs) {
						if(vout.getSeqNum() != vins[i].getVoutNum()) {
							newOutputs.add(vout);
						}
					}
					System.out.println("new:" + newOutputs.size());
					if(newOutputs.size() == 0) {
						Global.utxoDB.del(vins[i].getTxid());
					}else {
						Global.utxoDB.put(vins[i].getTxid(), newOutputs);
					}
				}
			}
			
			HashSet<Vout> vouts = new HashSet<>();
			for(int i = 0;i<tx.getVout().length;i++) {
				vouts.add(tx.getVout()[i]);
			}
			Global.utxoDB.put(tx.getTxid(), vouts);
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
	public static HashMap<String,HashSet<Vout>> FindAllUTXO(Blockchain blockchain){
		HashMap<String,HashSet<Vout>> UTXO = new HashMap<>();
		HashMap<String,HashSet<Integer>> spentTXOs = new HashMap<>();
		Iterator<Block> iterator = blockchain.iterator();
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
							if(vout.getSeqNum() == vins[i].getVoutNum()) {
								temp.remove(vout);
								if(temp.size() == 0)	UTXO.remove(vins[i].getTxid());
								break;
							}
						}
					}
					if(!spentTXOs.containsKey(vins[i].getTxid())) {
						spentTXOs.put(vins[i].getTxid(), new HashSet<>());
					}
					spentTXOs.get(vins[i].getTxid()).add(vins[i].getVoutNum());
				}
				Vout[] vouts = tx.getVout();
				VoutLoop:for(int i = 0; i < vouts.length; i++) {
					if(!spentTXOs.containsKey(txID)) {
						if(UTXO.get(txID) == null) {
							UTXO.put(txID, new HashSet<Vout>());
						}
						UTXO.get(txID).add(vouts[i]);
					}
//					else {
//						for(int index : spentTXOs.get(txID)) {
//							if(index == vouts[i].getSeqNum()) {
//								spentTXOs.get(txID).remove(vouts[i].getSeqNum());
//								continue VoutLoop;
//							}
//						}
//						UTXO.get(txID).add(vouts[i]);
//					}
				}
			}	
		}
		return UTXO;
	}
	
	public static ArrayList<Transaction> sortTransaction(ArrayList<Transaction> transactionArray) {
		for(int i = 0 ; i < transactionArray.size(); i++) {
			for(int j = i; j<transactionArray.size(); j++) {
				if(transactionArray.get(i).getTimestamp().after(transactionArray.get(j).getTimestamp())) {
					Transaction tempTransaction = transactionArray.get(j);
					transactionArray.set(j, transactionArray.get(i));
					transactionArray.set(i, tempTransaction);
				}
			}
		}
		return transactionArray;
	}

}
