package Test;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Stack;

import com.mongodb.internal.connection.tlschannel.util.Util;

import Security.KeyValuePairs;
import Utilities.Utilities;
import obj.*;

class Hello implements Serializable{
	public int a;
	public int b;
}

class XjmTest {
	
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
				if(KeyValuePairs.Verify(tx2.toString(), temp1, temp)) count++;
			}
		}
		if(count == tx2.getVin().length) return true;
		return false;
	}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Hello hello1 = new Hello();
		hello1.a = 1;
		hello1.b = 2;
		String helloString = Utilities.toByteArray(hello1).toString();
		Hello hello2 = (Hello) Utilities.toObject(helloString.getBytes());
		System.err.println(hello2.a);
		
	}

}
