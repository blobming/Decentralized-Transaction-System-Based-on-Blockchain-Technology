package obj;

public class Vout {
	private int value;
	private int seqNum;
	private String pubHash; //hash of publickey
	
	@Override
	public String toString() {
		return ""+value+seqNum+pubHash;
	}
}
