package obj;

import java.util.Date;

import config.Global;

public class Block {
	private int preHashCode; 	// hash of previous block
	private int hashCode; 		// hash of current block
	private int merkleRootHash; // hash of the root node of the merkleTree
	private Date timeStamp;
	private int nBits;			// the difficulty of mining
	private int nonce;			// Be used to calculate satisfied hash
	private BlockBody blockBody;
	
	public int getPreHashCode() {
		return preHashCode;
	}

	public int getHashCode() {
		return hashCode;
	}

	public int getMerkleRootHash() {
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
	
	public Block(int preHashCode, int merkleRootHash, int nonce) {
		this.preHashCode = preHashCode;
		this.merkleRootHash = merkleRootHash;
		this.timeStamp = new Date();
		this.nBits = Global.getInstance().nbits;
		this.nonce = nonce;
		this.hashCode = this.calculateHash();
	}
	
	// use preHashCode, merkleRootHash, timeStamp, 
	// nBits and nonce to calculate the hash of current block
	public int calculateHash() {
		String content = "" + 
						this.preHashCode +
						this.merkleRootHash +
						this.timeStamp +
						this.nBits +
						this.nonce;
		return content.hashCode();
	}
	
	
	
	
}
