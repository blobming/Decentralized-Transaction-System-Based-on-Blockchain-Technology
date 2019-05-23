package database;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;

import config.Global;
import obj.Block;
import utilities.Utilities;


/*
 * 
 * 备忘：存储block的数据库名为Block
 * 存储utxo的数据库名为UTXO
 * 交易池数据库名为TXPool
 * 
 */

public class Test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Global.blockDB.open("Block");
		Global.blockDB.open("Config");  //Height of the current Block Chain
		Global.utxoDB.open("UTXO");
		Block b = (Block)Global.blockDB.get("35423881ba906984319ac95bc471218ba8cb161b0717315118380e5efdd2aab9");
		System.out.println(b.getHashCode());	
	}

}
