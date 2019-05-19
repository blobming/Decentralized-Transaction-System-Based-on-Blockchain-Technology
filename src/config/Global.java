package config;

import Security.KeyValuePairs;
import database.MyBerkeleyDB;
import obj.User;
import userInterface.BlockChainEntrance;

public class Global {
	public static final int maxBlockTxNum = 16;
	public static final int minBlockTxNum = 1;
	public static final double genesisTransaction = 100.0;
	public static final int nbits = 4; //用于与其他节点同步获取合适的nbits
	public static final double subsidy = 0.02;
	public static MyBerkeleyDB utxoDB = new MyBerkeleyDB("./DataFile/UTXO");
	public static MyBerkeleyDB blockDB = new MyBerkeleyDB("./DataFile/Block");
	public static MyBerkeleyDB txDB = new MyBerkeleyDB("./DataFile/TXPool");
	public static KeyValuePairs keyValuePairs;
	public static String genesisTX = "ffd9cc40d8f7b19eae035fd17f9e79e2d38e4395e019118ffdee02c5136f0e72";
	public static User user;
	public static BlockChainEntrance blockChainEntrance;
}