package berkeleyDb;

import java.io.UnsupportedEncodingException;

/*
 * 
 * 备忘：存储block的数据库名为Block
 * 存储utxo的数据库名为UTXO
 * 
 */
public class Test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyBerkeleyDB blockDB = new MyBerkeleyDB("./Block");
		MyBerkeleyDB utxoDB = new MyBerkeleyDB("./UTXO");
		
		blockDB.open("Block");
		blockDB.put("2345", "zx");
		
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
	}

}
