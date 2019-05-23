package obj;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Stack;

import Security.KeyValuePairs;
import config.Global;
import utilities.Utilities;

public class Transaction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3942888348848503303L;
	private String txid;
	private String hash;
	private Vin[] vin;
	private Vout[] vout;
	private Date timestamp;
	private boolean isCoinBase;
	
	public Transaction(Vin[] vin, Vout[] vout, Boolean isCoinBase, String payerPrivateKey, Date timeStamp) {
		this.vin = vin;
		this.vout = vout;
		setTimestamp(timeStamp);
		String toHash = "";
		for(Vin in : vin) {
			toHash += in.toString();
		}
		for(Vout out : vout) {
			toHash += out.toString();
		}
		toHash += getTimestamp().toString();
		this.hash = Utilities.hashKeyForDisk(toHash);
		this.txid = Utilities.hashKeyForDisk(toHash);
		this.isCoinBase = isCoinBase;
		if(!isCoinBase) {
			String singature = KeyValuePairs.Sign(this.toString(), payerPrivateKey);
			for(Vin in : vin) {
				in.setSignature(singature);
			}
		}
	}
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
	
	public boolean isCoinBase() {
		return isCoinBase;
	}
	public void setCoinBase(boolean isCoinBase) {
		this.isCoinBase = isCoinBase;
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
	
	public static boolean validateTransaction(Transaction tx, Block block) {
		int count = 0;
		for(Vin vin:tx.vin) {
			//Get the vout which links to current vin
			//Transaction transaction = Transaction.GetTransactionById(vin.getTxid());
			HashSet<Vout> voutList = Vout.FindVoutByTransactionId(vin.getTxid());
			//得到相应的vout
			Vout vout = null;
			if(voutList != null) {
				for(Vout tempVout : voutList) {
					if(tempVout.getSeqNum() == vin.getVoutNum()) {
						vout = tempVout;
						break;
					}
				}
			}else {
				if(block == null) {
					return false;
				}
				for(Transaction transaction:block.getBlockBody().transactions) {
					if(transaction.getTxid().equals(vin.getTxid())) {
						for(Vout tempVout : transaction.vout) {
							if(tempVout.getSeqNum() == vin.getVoutNum()) {
								vout = tempVout;
								break;
							}
						}
					}
				}
			}
			if(vout == null) {
				return false;
			}
			
			//交易验证通过
			//Vout vout = transaction.getVout()[vin.getVoutNum()]; 
			Stack<String> stack = new Stack<String>();
			
			stack.push(vin.getSignature());
			stack.push(vin.getPublickey());
			stack.push(vin.getPublickey());
			
			String temp = Utilities.hashKeyForDisk(stack.pop());
			stack.push(temp);
			
			stack.push(vout.getPubHash());
			temp = stack.pop();
			if(temp.equals(stack.pop())) {
				System.out.println("初步匹配相等");
				temp = stack.pop();
				String temp1 = stack.pop();
				System.out.println(temp);
				System.out.println(temp1);
				if(KeyValuePairs.Verify(tx.toString(), temp1, temp)) count++;
			}
		}
		if(count == tx.vin.length) {
			System.out.println("交易验证通过");
			return true;
		}
		return false;
	}
	
	public static Transaction newCoinbaseTx(String Toaddress){
		Vin vin = new Vin("",-1, null);
		//to address取成哈希
		Vout vout = new Vout(Global.subsidy, 0, Utilities.hashKeyForDisk(Toaddress));
		Vin vins[] = new Vin[1];
		vins[0] = vin;
		Vout vouts[] = new Vout[1];
		vouts[0] = vout;
		Transaction t = new Transaction(vins, vouts, true, null, new Date());
		//t.isCoinBase = true;
		return t;
	}
	public static Transaction genesisCoinbaseTx(String Toaddress){
		Vin vin = new Vin("", -1, null);
		//to address取成哈希
		Vout vout = new Vout(Global.genesisTransaction, 0, Utilities.hashKeyForDisk(Toaddress));
		Vin vins[] = new Vin[1];
		vins[0] = vin;
		Vout vouts[] = new Vout[1];
		vouts[0] = vout;
		Date timeStamp = new Date();
		try {
			timeStamp = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Transaction t = new Transaction(vins, vouts, true, null,timeStamp);
		return t;
	}
	public static Transaction createTransaction(String payerPubKey, String payerPrivateKey, String payeePubKeyHash, Double amount, Date transactionDate) {
		double total = 0.0;
		HashMap<String, HashSet<Vout>> spendable = UTXOSet.FindSpendableOutputs(payerPubKey, amount);
		ArrayList<Vin> vins = new ArrayList<>();
		for(Entry<String, HashSet<Vout>> entry : spendable.entrySet()) {
			String txID = entry.getKey();
			for(Vout vout : entry.getValue()) {
				total += vout.getValue();
				vins.add(new Vin(txID, vout.getSeqNum(), payerPubKey));
			}
		}
		Vin[] vinList = new Vin[vins.size()];
		System.out.println("ToalAmount111!!!"+total);
		for(int i=0;i<vinList.length;i++) {
			vinList[i] = vins.get(i);
		}
		Vout vout1 = new Vout(amount, 0, payeePubKeyHash);
		Vout vout2 = new Vout(total-amount, 1, Utilities.hashKeyForDisk(payerPubKey));
		Vout[] vouts = new Vout[2];
		vouts[0] = vout1;
		vouts[1] = vout2;
		Transaction t = new Transaction(vinList, vouts, false, payerPrivateKey, transactionDate);
		return t;
	}
	
	public static Boolean checkTransaction(String payerPubKey, String payerPrivateKey, String payeePubKeyHash, Double amount) {
		double total = 0.0;
		HashMap<String, HashSet<Vout>> spendable = UTXOSet.FindSpendableOutputs(payerPubKey, amount);
		ArrayList<Vin> vins = new ArrayList<>();
		for(Entry<String, HashSet<Vout>> entry : spendable.entrySet()) {
			String txID = entry.getKey();
			for(Vout vout : entry.getValue()) {
				total += vout.getValue();
				vins.add(new Vin(txID, vout.getSeqNum(), payerPubKey));
			}
		}
		if(total < amount) {
			return false;
		}else {
			return true;
		}
	}
	
	@Override
	public String toString() {
		return "Transaction [txid=" + txid + ", hash=" + hash + ", vin=" + Arrays.toString(vin) + ", vout="
				+ Arrays.toString(vout) + ", timestamp=" + timestamp + ", isCoinBase=" + isCoinBase + "]";
	}
	
	
}
