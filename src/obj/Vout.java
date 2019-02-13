package obj;

public class Vout {
	private int value;
	private int seqNum;
	private String pubHash; //hash of publickey
	
	
	public Vout(int value, int seqNum, String pubHash) {
		this.value = value;
		this.seqNum = seqNum;
		this.pubHash = pubHash;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
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
		return ""+value+seqNum+pubHash;
	}
	
	// IsLockedWithKey checks if the output can be used by the owner of the pubkey
	public boolean IsLockedWithKey(String publicKeyHash) {
		return this.pubHash.equals(publicKeyHash);
	}
}
