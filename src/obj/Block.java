package obj;

import java.io.Serializable;
import java.util.Date;

import config.Global;
import utilities.Utilities;

public class Block implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public void setNonce(int nonce) {
		this.nonce = nonce;
	}
	
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public BlockBody getBlockBody() {
		return blockBody;
	}
	
	public Block(BlockBody blockBody, int nonce, String preHashCode, Date timeStamp) {
		this.preHashCode = preHashCode;
		this.merkleRootHash = blockBody.getRoot().getHash();
		this.timeStamp = timeStamp;
		this.nBits = Global.nbits;
		this.nonce = nonce;
		this.hashCode = this.calculateHash();
		this.blockBody = blockBody;
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
