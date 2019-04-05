package obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import berkeleyDb.MyBerkeleyDB;
import config.Global;

public class UTXOSet {
	private static Blockchain blockchain;
	//public FindSpendableOutputs(String address, int amount)
	public static ArrayList<Vout> FindUTXO(String address) {
		ArrayList<Vout> utxo = new ArrayList<Vout>();
		for(Entry<String, Object> entry : Global.utxoDB.getAllKey().entrySet()) {
			HashSet<Vout> vouts = (HashSet<Vout>) entry.getValue();
			for(Vout vout : vouts) {
				if(vout.IsLockedWithKey(address)) {
					utxo.add(vout);
				}
			}
		}
		return utxo;
	}
	
	public static HashMap<String, HashSet<Vout>> FindSpendableOutputs(String address, double amount) {
		HashMap<String, HashSet<Vout>> unspentOutputs = new HashMap<>();
		double accumulated = 0;
		for(Entry<String, Object> entry : Global.utxoDB.getAllKey().entrySet()) {
			HashSet<Vout> vouts = (HashSet<Vout>) entry.getValue();
			for(Vout vout : vouts) {
				if(vout.IsLockedWithKey(address) && accumulated < vout.getValue()) {
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
	public static void Reindex() {
		Global.utxoDB.getEnvironment().truncateDatabase(null, "UTXO", false);
		HashMap<String,HashSet<Vout>> utxo = Blockchain.FindUTXO(blockchain.iterator());
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
		ArrayList<Transaction> txs = blockbody.transactions;
		for(Transaction tx : txs) {
			if(!tx.isCoinBase()) {
				Vin[] vins = tx.getVin();
				for(int i=0;i<vins.length;i++) {
					HashSet<Vout> outputs = (HashSet<Vout>) Global.utxoDB.get(vins[i].getTxid());
					HashSet<Vout> newOutputs = new HashSet<>();
					for(Vout vout : outputs) {
						if(vout.getSeqNum() != vins[i].getVoutNum()) {
							newOutputs.add(vout);
						}
					}
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
	public static HashSet<Vout> FindVoutByTransactionId(String txId){
		
		Object voutList = Global.utxoDB.get(txId);
		if(voutList instanceof HashSet<?>) {
			return (HashSet<Vout>) Global.utxoDB.get(txId);
		}
		return null;
		
	}
}
