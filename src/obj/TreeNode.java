package obj;

public class TreeNode {
	private String tx1Hash;
	private String tx2Hash;
	private Transaction tx1;
	private Transaction tx2;
	
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
	public Transaction getTx1() {
		return tx1;
	}
	public void setTx1(Transaction tx1) {
		this.tx1 = tx1;
	}
	public Transaction getTx2() {
		return tx2;
	}
	public void setTx2(Transaction tx2) {
		this.tx2 = tx2;
	}
	public TreeNode(String tx1Hash, String tx2Hash) {
		this.tx1Hash = tx1Hash;
		this.tx2Hash = tx2Hash;
	}
	public TreeNode(String tx1Hash, String tx2Hash, Transaction tx1,Transaction tx2) {
		this.tx1Hash = tx1Hash;
		this.tx2Hash = tx2Hash;
		this.tx1 = tx1;
		this.tx2 = tx2;
	}
}
