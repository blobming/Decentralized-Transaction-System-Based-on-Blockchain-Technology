package Test;
import java.util.HashMap;

import obj.*;

public class XjmTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//HashMap<String,String> tempMap = (HashMap<String, String>) Utilities.Utilities.getInternetIp();
		//for(String key:tempMap.keySet()) {
		//	System.out.println("key: "+key+"   "+"ip"+tempMap.get(key));
		//}
		//System.out.println(tempMap.get("a"));
		
		Vout 
		
		//给A 十块钱
		Transaction coinBasTransaction = new Transaction(vin, vout);
		
		//生成A -> B的交易
		Transaction aToBTransaction = new Transaction(vin,vout);
		
		System.out.println(Transaction.validateTransaction(aToBTransaction));
		
	}

}
