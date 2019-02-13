package berkeleyDb;
import java.io.UnsupportedEncodingException;

public class Test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyBerkeleyDB dbInstance = MyBerkeleyDB.GetInstance();
		
		dbInstance.open("Test");
		//dbInstance.put("123", "456");
		dbInstance.put("2345", "789");
		
		for(String a:dbInstance.getAllKey()) {
			System.out.println(a);
			try {
				System.out.println(dbInstance.get(a));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dbInstance.close();
	}

}
