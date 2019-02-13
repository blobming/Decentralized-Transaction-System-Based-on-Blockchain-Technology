package berkeleyDb;
/*
 * 
 * 备忘：存储block的数据库名为Block
 * 存储utxo的数据库名为UTXO
 * 
 */
public class Test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyBerkeleyDB dbInstance = MyBerkeleyDB.GetInstance();
		
		dbInstance.open("Test");
		
		//dbInstance.put("123", "456");
		dbInstance.put("2345", "789");
		
		for(String a:dbInstance.getAllKey()) {
			System.out.println(a);
			System.out.println(dbInstance.get(a));
		}
		dbInstance.close();
	}

}
