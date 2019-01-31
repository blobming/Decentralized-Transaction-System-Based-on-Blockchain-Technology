package obj;

import java.util.Date;

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
	
	public boolean validateTransaction(Transaction tx) {
		return false;
	}
	
	
}
