package obj;

import java.io.Serializable;
import java.util.HashSet;

import config.Global;

public class Vout  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4342461471962570533L;
	private double value;
	private int seqNum;     //表示这是这个交易里的第几个输出
	private String pubHash; //hash of payee's publickey
	
	
	public Vout(double value, int seqNum, String pubHash) {
		this.value = value;
		this.seqNum = seqNum;
		this.pubHash = pubHash;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public String getPubHash() {
		return pubHash;
	}

	public void setPubHash(String pubHash) {
		this.pubHash = pubHash;
	}
	
	@Override
	public String toString() {
		return "Vout [value=" + value + ", seqNum=" + seqNum + ", pubHash=" + pubHash + "]";
	}

	// IsLockedWithKey checks if the output can be used by the owner of the pubkey
	public boolean IsLockedWithKey(String publicKeyHash) {
		return this.pubHash.equals(publicKeyHash);
	}
	
	public static HashSet<Vout> FindVoutByTransactionId(String txId){
		
		Object voutList = Global.utxoDB.get(txId);
		if(voutList instanceof HashSet<?>) {
			return (HashSet<Vout>) Global.utxoDB.get(txId);
		}
		return null;
		
	}
}
