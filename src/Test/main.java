package Test;

import java.io.UnsupportedEncodingException;

import Utilities.Utilities;
import berkeleyDb.MyBerkeleyDB;
import obj.Block;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		 * 
		 * 一个全节点的功能
		 * 
		 * 	if(数据库中已存在现有区块链)
		 * 		-> 取出现有区块链
		 *  else
		 *		-> 创建第一个块(创世块)
		 *  	-> 初始化区块链
		 *  
		 *  -> 生成UTXO
		 * 	-> 开启网络
		 * 		-> 向邻居发送握手消息，确认版本号与区块链长度
		 * 		-> 邻居回应后更新自己的区块链版本
		 * 		-> 如果自己的区块链比别人的短 向别人广播请求多出来的块
		 * 		-> 接收区块
		 * 		-> if(块已存在||验证不通过)验证比较重要 通过后更改UTXO,修改余额
		 * 			-> 不添加块
		 * 		-> else
		 * 			-> 将块添加到区块链中
		 * 		-> 接收discoverip地址请求向别人发送ip
		 * 		-> 对外请求ip地址
		 *	-> 对外提供RPC服务
		 *
		 *  -> 矿工节点(optional 如果有愿意当矿工的节点可以选择此选项)
		 *  	-> 接收交易
		 * 			->将交易放入交易池里面
		 * 			->检查交易是否到达n个
		 * 				->组装成 merkle Tree 打包成block
		 * 		-> 挖block，在挖的过程中不断接收新的区块
		 * 		-> 如果发现接收到的区块验证通过并加到区块链里时，新的区块中有与(在正在被挖矿的区块中)相同的交易时
		 * 			-> 放弃挖矿，重新选择交易继续挖矿
		 * 		-> 如果块挖好了 将块打包发布
		 * 
		 * 一个轻量级节点的功能
		 * 	->连接RPC
		 *  	-> 正常的查看区块链
		 *  	-> 转账
		 *  	-> 依托全节点验证区块
		 *  	-> 查看自己的余额->通过自己的公钥的哈希
		 */
		
		
		//MyBerkeleyDB dbInstance = MyBerkeleyDB.GetInstance();
		//dbInstance.setEnvironment("./Data");
		/*
		dbInstance.open("Test");
		dbInstance.put("b"+block.getHashCode(), Utilities.toByteArray(block));
		for(String a:dbInstance.getAllKey()) {
			System.out.println(a);
			System.out.println(a.getClass());
			System.out.println(dbInstance.get(a).getClass());
			
			
			//if(a.getClass()=="Block.class") {
				
			//}
			
			//Block block1 = (Block)dbInstance.get(a);
			//if(block1 != null) {
			//	System.out.println(block1.getHashCode());
			//}
		}*/
		//dbInstance.close();
		
		
		
	}

}
