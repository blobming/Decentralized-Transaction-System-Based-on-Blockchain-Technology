package obj;

import java.util.ArrayList;

import config.Global;

public class TXPool {
	public static void putInPool(Transaction t) {
		Global.txDB.put(t.getHash(), t);
	}
	public static void removeTX(Transaction t) {
		Global.txDB.del(t.getHash());
	}
	public static void removeTX(String tHash) {
		Global.txDB.del(tHash);
	}
	public static boolean contains(Transaction t) {
		if(Global.txDB.get(t.getHash()) != null)	return true;
		return false;
	}
	public static boolean contains(String t) {
		if(Global.txDB.get(t) != null)	return true;
		return false;
	}
	public static Transaction get(String hashcode) {
		return (Transaction) Global.txDB.get(hashcode);
	}
	//FIXME
	/*
	public static ArrayList<Transaction> getAll(){
		ArrayList<Transaction> txs = new ArrayList<>();
		return txs;
	}*/
	public static ArrayList<String> getAllHash(){
		ArrayList<String> res = new ArrayList<>();
		for(String s: Global.txDB.getAllKey().keySet())	res.add(s);
		return res;
	}
	public static void removeAll(ArrayList<String> hashcodes) {
		for(String s : hashcodes) {
			removeTX(s);
		}
	}
	//这个函数用于矿工节点随机从交易池中取出交易
	public static ArrayList<Transaction> gatherTransaction(){
		ArrayList<String> hashList = TXPool.getAllHash();
		ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
		int i = 0;
		for(String s : hashList) {
			transactionList.add(TXPool.get(s));
			i += 1;
			if(i == Global.maxBlockTxNum) {
				break;
			}
		}
		if(transactionList.size() == 0) return transactionList;
		transactionList = UTXOSet.sortTransaction(transactionList);
		if(transactionList.size() % 2 == 0) {
			transactionList.remove(transactionList.size()-1);
		}
		return transactionList;
	}
}
