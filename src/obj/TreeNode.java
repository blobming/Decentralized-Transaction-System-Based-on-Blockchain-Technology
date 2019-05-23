package obj;

import java.io.Serializable;

import utilities.Utilities;

public class TreeNode implements Serializable {
	/**
	 * 更改了在创世快的情况，即第二个构造函数。在创世快中，只有一个交易，因此treenode也只有一个。
	 * FIXME：另，现在block中的交易不需要arrayList<TX>了，可以直接用treenode[]的倒数的部分来获取到
	 * 用倒数的原因是倒着存储treeNode的
	 * 
	 * FIXME 在新建block的时候所有的transaction按照timestamp由旧到新组成
	 */
	private static final long serialVersionUID = -960926854704582471L;
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
		this.tx2Hash = null;
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
	
	@Override
	public String toString() {
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

