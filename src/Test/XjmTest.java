package Test;
import java.util.HashMap;

public class XjmTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String,String> tempMap = (HashMap<String, String>) Utilities.Utilities.getInternetIp();
		for(String key:tempMap.keySet()) {
			System.out.println("key: "+key+"   "+"ip"+tempMap.get(key));
		}
		System.out.println(tempMap.get("a"));
	}

}
