package berkeleyDb;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

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
		blockDB.put("2345", "zx");
		utxoDB.open("UTXO");
	utxoDB.put(12, h);
		H dbH = (H) utxoDB.get(12);
		System.out.println(dbH.name);
	
		System.out.println(blockDB.get("2345"));
		try {
			for(String a:blockDB.getAllKey()) {
		//		System.out.println(a);
			//	System.out.println(blockDB.get(a));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		blockDB.close();
		String a = "2345";
		
	}

}
