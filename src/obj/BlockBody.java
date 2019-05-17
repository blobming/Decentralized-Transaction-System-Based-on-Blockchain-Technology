package obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.google.gson.Gson;

import config.Global;

public class BlockBody  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9157268033609669878L;
	private TreeNode[] nodes;
	private TreeNode root;
	public ArrayList<Transaction> transactions;
	
	public BlockBody(ArrayList<Transaction> txs) {
		this.transactions = txs;
		if(txs.size() > Global.maxBlockTxNum || txs.size() < Global.minBlockTxNum)	throw new java.lang.IllegalArgumentException("invalid tx num");
		TreeNode[] nodes = new TreeNode[txs.size() * 2 -1];
		Queue<TreeNode> queue = new LinkedList<>();
		int index = txs.size() * 2 - 2;
		for(int i=0;i<txs.size();i++, index--) {
			TreeNode n = new TreeNode(txs.get(i));
			nodes[index] = n;
			queue.add(n);
		}
		while(queue.size() != 1) {
			TreeNode n1 = queue.poll();
			TreeNode n2 = queue.poll();
			TreeNode father = new TreeNode(n1,n2);
			n1.setFather(father);
			n2.setFather(father);
			queue.add(father);
			nodes[index--] = father;
		}
		this.root = queue.poll();
		root.setFather(null);
		nodes[0] = root;
		this.nodes = nodes;
	}
	
	public BlockBody(Transaction coinbaseTX) {
		ArrayList<Transaction> txs = new ArrayList<>();
		txs.add(coinbaseTX);
		this.transactions = txs;
		TreeNode[] nodes = new TreeNode[1];
		nodes[0] = new TreeNode(coinbaseTX);
		this.root = nodes[0];
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
	public void browseWholeTree(){
		System.out.println(new Gson().toJson(this.nodes));
	}
}
