package obj;

import java.util.Date;

public class Transaction {
	private String txid;
	private String hash;
	private Vin vin;
	private Vout vout;
	private Date timestamp;
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public Vin getVin() {
		return vin;
	}
	public void setVin(Vin vin) {
		this.vin = vin;
	}
	public Vout getVout() {
		return vout;
	}
	public void setVout(Vout vout) {
		this.vout = vout;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Transaction(String hash, Vin vin, Vout vout) {
		this.hash = hash;
		this.vin = vin;
		this.vout = vout;
		setTimestamp(new Date());
	} 
	public boolean validateTransaction(Transaction tx) {
		return false;
	}
	
	
}
