package Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import Security.KeyValuePairs;
import Utilities.Utilities;
import obj.*;

public class XjmTest {
	
	public static boolean validateTransaction(Transaction tx1 ,Transaction tx2) {
		int count = 0;
		for(Vin vin:tx2.getVin()) {
			//Get the vout which links to current vin
			//Transaction transaction = Transaction.GetTransactionById(vin.getTxid()); 
			HashSet<Vout> voutList = new HashSet<Vout>();
			voutList.add(tx1.getVout()[0]);
			
			Vout vout = null;
			for(Vout tempVout : voutList) {
				if(tempVout.getSeqNum() == vin.getVoutNum()) {
					vout = tempVout;
					break;
				}
			}
			if(vout == null) {
				return false;
			}
			System.out.println("对暗号成功");
			//Vout vout = transaction.getVout()[vin.getVoutNum()]; 
			Stack<String> stack = new Stack<String>();
			
			
			stack.push(vin.getSignature());
			stack.push(vin.getPublickey());
			stack.push(vin.getPublickey());
			
			String temp = Utilities.hashKeyForDisk(stack.pop());
			stack.push(temp); //vin 花这个钱的时候公钥哈希
			
			stack.push(vout.getPubHash()); //输出脚本的收款人公钥的哈希
			temp = stack.pop();
			if(temp.equals(stack.pop())) {
				System.out.println("初步匹配相等");
				temp = stack.pop();
				String temp1 = stack.pop();
				System.out.println(temp);
				System.out.println(temp1);
				System.out.println(new KeyValuePairs().Verify(tx2.toString(), temp1, temp));
				//if(temp.equals(stack.pop())) count++ ;
			}
		}
		if(count == tx2.getVin().length) return true;
		return false;
	}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//HashMap<String,String> tempMap = (HashMap<String, String>) Utilities.Utilities.getInternetIp();
		//for(String key:tempMap.keySet()) {
		//	System.out.println("key: "+key+"   "+"ip"+tempMap.get(key));
		//}
		//System.out.println(tempMap.get("a"));
		
		KeyValuePairs keyValuePairsA = new KeyValuePairs();
		KeyValuePairs keyValuePairsB = new KeyValuePairs();
		
		//给A 十块钱
		Transaction transaction =  Transaction.genesisCoinbaseTx(Utilities.hashKeyForDisk(keyValuePairsA.getPublicKey()));
		
		//生成A -> B的交易
		Vin vin = new Vin(null, 0, keyValuePairsA.getPublicKey());
		Vout vout0 = new Vout(10,0,Utilities.hashKeyForDisk(keyValuePairsB.getPublicKey()));
		Vout vout1 = new Vout(90,0,Utilities.hashKeyForDisk(keyValuePairsA.getPublicKey()));
		Vin[] vinList = {vin};
		Vout[] voutList = {vout0,vout1};
		Transaction aToBTransaction = new Transaction(vinList,voutList);
		vin.setTxid(aToBTransaction.getTxid());
		System.out.println(keyValuePairsA.getPublicKey());
		System.out.println(keyValuePairsA.Sign(aToBTransaction.toString()));
		
		vin.setSignature(keyValuePairsA.Sign(aToBTransaction.toString()));
		
		//System.out.print(keyValuePairsA.Verify(aToBTransaction, keyValuePairsA.Sign(aToBTransaction.toString()), keyValuePairsA.getPublicKey()));
		
		
		//System.out.println(validateTransaction(transaction,aToBTransaction));
		
	}

}
