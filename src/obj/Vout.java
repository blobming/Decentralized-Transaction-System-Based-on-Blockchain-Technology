package obj;

public class Vout {
	
	
	private int value;
	private int seqNum;
	private String pubHash; //hash of publickey
	
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
}
