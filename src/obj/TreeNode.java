package obj;

import Utilities.Utilities;

public class TreeNode {
	private String tx1Hash;
	private String tx2Hash;
	private Transaction tx;
	private TreeNode father;
	
	public TreeNode(TreeNode n1, TreeNode n2) {
		this.tx1Hash = n1.getHash();
		this.tx2Hash = n2.getHash();
		this.tx = null;
	}
	
	public TreeNode(Transaction tx) {
		this.tx = tx;
		this.tx1Hash = null;
		this.tx1Hash = null;
	}

	public String getTx1Hash() {
		return tx1Hash;
	}

	public void setTx1Hash(String tx1Hash) {
		this.tx1Hash = tx1Hash;
	}

	public String getTx2Hash() {
		return tx2Hash;
	}

	public void setTx2Hash(String tx2Hash) {
		this.tx2Hash = tx2Hash;
	}

	public Transaction getTx() {
		return tx;
	}

	public void setTx(Transaction tx) {
		this.tx = tx;
	}

	public TreeNode getFather() {
		return father;
	}

	public void setFather(TreeNode father) {
		this.father = father;
	}

	public String toStirng() {
		String res = "";
		if(tx == null) {
			res += tx1Hash;
			res += tx2Hash;
		}else {
			res += tx.getHash();
		}
		return res;
	}
	
	public String getHash() {
		return Utilities.hashKeyForDisk(toString());
	}
}

