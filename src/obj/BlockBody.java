package obj;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import config.Global;

public class BlockBody{
	private TreeNode[] nodes;
	private TreeNode root;
	public ArrayList<Transaction> transactions;
	
	public BlockBody(ArrayList<Transaction> txs) {
		this.transactions = txs;
		if(txs.size() > Global.maxBlockTxNum || txs.size() < Global.minBlockTxNum)	throw new java.lang.IllegalArgumentException("invalid tx num");
		TreeNode[] nodes = new TreeNode[txs.size()];
		Queue<TreeNode> queue = new LinkedList<>();
		for(int i=0;i<txs.size();i=i++) {
			TreeNode n = new TreeNode(txs.get(i));
			nodes[i] = n;
			queue.add(n);
		}
		this.nodes = nodes;
		while(queue.size() != 1) {
			TreeNode n1 = queue.poll();
			TreeNode n2 = queue.poll();
			TreeNode father = new TreeNode(n1,n2);
			n1.setFather(father);
			n2.setFather(father);
			queue.add(father);
		}
		this.root = queue.poll();
		root.setFather(null);
	}
	
	
	public BlockBody(Transaction coinbaseTX) {
		ArrayList<Transaction> txs = new ArrayList<>();
		txs.add(coinbaseTX);
		
		this.transactions = txs;
		TreeNode[] nodes = new TreeNode[txs.size()];
		Queue<TreeNode> queue = new LinkedList<>();
		for(int i=0;i<txs.size();i++) {
			TreeNode n = new TreeNode(txs.get(i));
			nodes[i] = n;
			queue.add(n);
		}
		this.nodes = nodes;
		while(queue.size() != 1) {
			TreeNode n1 = queue.poll();
			TreeNode n2 = queue.poll();
			TreeNode father = new TreeNode(n1,n2);
			n1.setFather(father);
			n2.setFather(father);
			queue.add(father);
		}
		this.root = queue.poll();
		root.setFather(null);
	}

	public TreeNode[] getNodes() {
		return nodes;
	}

	public void setNodes(TreeNode[] nodes) {
		this.nodes = nodes;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}
	
	
}
