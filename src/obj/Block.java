package obj;

import java.util.Date;

import Utilities.Utilities;
import config.Global;

public class Block {
	private String preHashCode; 	// hash of previous block
	private String hashCode; 		// hash of current block
	private String merkleRootHash; // hash of the root node of the merkleTree
	private Date timeStamp;
	private int nBits;			// the difficulty of mining
	private int nonce;			// Be used to calculate satisfied hash
	private BlockBody blockBody;
	
	public String getPreHashCode() {
		return preHashCode;
	}

	public void setPreHashCode(String preHashCode) {
		this.preHashCode = preHashCode;
	}

	public String getHashCode() {
		return hashCode;
	}

	public String getMerkleRootHash() {
		return merkleRootHash;
	}

	public Date getTimestamp() {
		return timeStamp;
	}

	public int getnBits() {
		return nBits;
	}

	public int getNonce() {
		return nonce;
	}

	public BlockBody getBlockBody() {
		return blockBody;
	}
	
	public Block(String preHashCode, String merkleRootHash, int nonce) {
		this.preHashCode = preHashCode;
		this.merkleRootHash = merkleRootHash;
		this.timeStamp = new Date();
		this.nBits = Global.getInstance().nbits;
		this.nonce = nonce;
		this.hashCode = this.calculateHash();
	}
	
	// use preHashCode, merkleRootHash, timeStamp, 
	// nBits and nonce to calculate the hash of current block
	public String calculateHash() {
		String content = "" + 
						this.preHashCode +
						this.merkleRootHash +
						this.timeStamp +
						this.nBits +
						this.nonce;
		return Utilities.hashKeyForDisk(content);
	}
	
}
