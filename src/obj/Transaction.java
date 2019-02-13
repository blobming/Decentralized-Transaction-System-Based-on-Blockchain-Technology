package obj;

import java.util.Date;
import java.util.Stack;

import Utilities.Utilities;

public class Transaction {
	private String txid;
	private String hash;
	private Vin[] vin;
	private Vout[] vout;
	private Date timestamp;
	public String getTxid() {
		return txid;
	}
	public String getHash() {
		return hash;
	}
	public Vin[] getVin() {
		return vin;
	}
	public void setVin(Vin[] vin) {
		this.vin = vin;
	}
	public Vout[] getVout() {
		return vout;
	}
	public void setVout(Vout[] vout) {
		this.vout = vout;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Transaction(Vin[] vin, Vout[] vout) {
		this.vin = vin;
		this.vout = vout;
		setTimestamp(new Date());
		String toHash = "";
		for(Vin in : vin) {
			toHash += in.toString();
		}
		for(Vout out : vout) {
			toHash += out.toString();
		}
		toHash += getTimestamp().toString();
		this.hash = toHash;
		this.txid = toHash;
	} 
	
	/* 执行过程
	 * 1. 先把签名压入栈
	 * 2. 把公钥压入栈
	 * 3. 把栈顶元素复制
	 * 4. 把栈顶元素弹出取哈希，再把公钥哈希压入栈(要花这个钱的时候提供的公钥)
	 * 5. 把输出脚本提供的(收款人，转给谁)公钥哈希值压入栈
	 * 6. 弹出栈顶两个元素，两个哈希值是否相等然后栈顶元素消失
	 * 7. 验证剩下两个元素是否正确
	*/
	
	public static boolean validateTransaction(Transaction tx) {
		int count = 0;
		for(Vin vin:tx.vin) {
			//Get the vout which links to current vin
			Transaction transaction = Transaction.GetTransactionById(vin.getTxid()); 
			//得到相应的vout
			Vout vout = transaction.getVout()[vin.getVoutNum()]; 
			Stack<String> stack = new Stack<String>();
			
			stack.push(vin.getSignature());
			stack.push(vin.getPublickey());
			stack.push(vin.getPublickey());
			
			String temp = Utilities.hashKeyForDisk(stack.pop());
			stack.push(temp);
			
			stack.push(vout.getPubHash());
			temp = stack.pop();
			if(temp.equals(stack.pop())) {
				temp = stack.pop();
				if(temp.equals(stack.pop())) count++ ;
			}
		}
		if(count == tx.vin.length) return true;
		return false;
	}
	public static Transaction GetTransactionById(String id) {
		
		return null;
	}
	public static Transaction newCoinbaseTx(String Toaddress,String pubkey){
		Vin vin = new Vin("",-1,pubkey);
		//to address取成哈希
		Vout vout = new Vout(subsidy,0,Toaddress);
		Vin vins[] = new Vin[0];
		vins[0] = vin;
		Vout vouts[] = new Vout[0];
		vouts[0] = vout;
		Transaction t = new Transaction(vins,vouts);
		return t;
	}
	
}
