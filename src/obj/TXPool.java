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
	
}
