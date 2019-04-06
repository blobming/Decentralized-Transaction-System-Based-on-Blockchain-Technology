package config;

import Security.KeyValuePairs;
import berkeleyDb.MyBerkeleyDB;

public class Global {
	private static Global global = new Global();
	
	public static final int maxBlockTxNum = 16;
	public static final int minBlockTxNum = 1;
	public static final double genesisTransaction = 100.0;
	public static final int nbits = 4; //用于与其他节点同步获取合适的nbits
	public static final double subsidy = 0.02;
	public static MyBerkeleyDB utxoDB = new MyBerkeleyDB("./UTXO");
	public static MyBerkeleyDB blockDB = new MyBerkeleyDB("./Block");
	public static KeyValuePairs keyValuePairs;
	public static String genesisTX;
}
