package berkeleyDb;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;

import Utilities.Utilities;


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
		MyBerkeleyDB blockDB = new MyBerkeleyDB("./Block");
		MyBerkeleyDB utxoDB = new MyBerkeleyDB("./UTXO");
		H h = new H();
		h.name = "hname";
		blockDB.open("Block");
		blockDB.put("2345", "zx");
		blockDB.put("1234", "zb");
		blockDB.put("1255", "bgg");
		
	
		System.out.println(blockDB.get("2345"));
		
		for(Entry<String, Object> entry:blockDB.getAllKey().entrySet()) {
			System.out.println((String)entry.getKey() + " " + (String)entry.getValue());
		//	System.out.println(blockDB.get(a));
		}
	
		blockDB.close();
		
	}

}
