package berkeleyDb;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;

import Utilities.Utilities;
import config.Global;


/*
 * 
 * 备忘：存储block的数据库名为Block
 * 存储utxo的数据库名为UTXO
 * 
 */
class H implements Serializable{
	String name;
}
public class Test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Global.blockDB.open("Block");
		Global.blockDB.open("Config");  //Height of the current Block Chain
		Global.utxoDB.open("UTXO");
		String s = (String)Global.blockDB.get("0");
		System.out.println(s);
	
		
		
	}

}
