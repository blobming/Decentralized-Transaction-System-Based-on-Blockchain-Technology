package obj;

import java.io.Serializable;

import Utilities.Utilities;

public class Vin  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4259754698211250314L;
	private String txid; //txid represents the hash of one specific transaction
	private int voutNum; // get the index of vout of txid
	private String signature; // use payer's private key to sign the whole transaction
	private String publickey; // payer's publickey
	
	public Vin(int voutNum, String publickey) {
		//this.txid = txid;
		this.voutNum = voutNum;
		this.publickey = publickey;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public int getVoutNum() {
		return voutNum;
	}

	public void setVoutNum(int voutNum) {
		this.voutNum = voutNum;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPublickey() {
		return publickey;
	}

	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}

	@Override
	public String toString() {
		return voutNum+publickey;
	}
	
	// UsesKey checks whether the address initiated the transaction
	public boolean UsesKey(String publickeyHash) {
		return Utilities.hashKeyForDisk(this.publickey).equals(publickeyHash);
	}
}