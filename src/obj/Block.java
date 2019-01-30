package obj;

import java.util.Date;

import config.Global;

public class Block {
	private int preHashCode; 	// hash of previous block
	private int hashCode; 		// hash of current block
	private int merkleRootHash; // hash of the root node of the merkleTree
	private Date timestamp;
	private int nBits;			// the difficulty of mining
	private int nonce;			// Be used to calculate sactisfied hash
	private BlockBody blockBody;
	
	public Block(int preHashCode, int merkleRootHash, int nonce) {
		this.preHashCode = preHashCode;
		this.merkleRootHash = merkleRootHash;
		this.timestamp = new Date();
		this.nBits = Global.nbits;
		this.nonce = nonce;
		this.hashCode = this.calculateHash();
	}
	
	// use preHashCode, merkleRootHash, timestamp, 
	// nBits and nounce to calculate the hash of current block
	public int calculateHash() {
		String content = "" + 
						this.preHashCode +
						this.merkleRootHash +
						this.timestamp +
						this.nBits +
						this.nonce;
		return content.hashCode();
	}
	
	
	
	
}
