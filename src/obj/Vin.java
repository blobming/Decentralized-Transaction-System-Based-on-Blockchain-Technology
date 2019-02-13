package obj;

public class Vin {
	private String txid; //txid represents the hash of one specific transaction
	private int voutNum; // get the index of vout of txid
	private String signature; // use payer's private key to sign the whole transaction
	private String publickey; // payer's publickey
	
	@Override
	public String toString() {
		return txid+voutNum+publickey;
	}
}


/* 执行过程
 * 1. 先把签名压入栈
 * 2. 把公钥压入栈
 * 3. 把栈顶元素复制
 * 4. 把栈顶元素弹出取哈希，再把公钥哈希压入栈(要花这个钱的时候提供的公钥)
 * 5. 把输出脚本提供的(收款人，转给谁)公钥哈希值压入栈
 * 6. 弹出栈顶两个元素，两个哈希值是否相等然后栈顶元素消失
 * 7. 验证剩下两个元素是否正确
*/