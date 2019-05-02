package Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharSet;
import org.apache.commons.lang3.StringUtils;

import Network.PeerNetwork;
import Network.PeerThread;
import Network.RpcServer;
import Network.RpcThread;
import Security.KeyValuePairs;
import Utilities.NetworkUtils;
import Utilities.Utilities;
import config.Global;
import obj.Block;
import obj.BlockBody;
import obj.Blockchain;
import obj.Transaction;
import obj.UTXOSet;
import obj.Vin;
import obj.Vout;

public class Main {
		/*
		 * 
		 * 一个全节点的功能														测试情况
		 * 	-> 提供RPC服务
		 * 		-> 发现IP️
		 * 		-> 成为矿工节点
		 * 		-> 发送转账信息
		 * 		-> 查看自己的余额
		 * 		-> 查看区块链情况 + 对交易信息的检验️
		 * 	-> 与其他节点通信️
		 * 		-> 与其他节点通信功能列举
		 * 			-> 1. 发送区块
		 * 			-> 2. 接收区块
		 * 			-> 3. 接收广播交易，生成交易池（如果是矿工节点）
		 * 			-> 4. 挖矿后广播新区块
		 * 			-> 5. 发送握手消息
		 * 			-> 6. 回复握手消息
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
		 * 		-> 广播discoverip请求向其他人请求ip地址
		 * 		-> 对外请求ip地址
		 *	-> 对外提供RPC服务
		 *
		 *  -> 矿工节点(optional 如果有愿意当矿工的节点可以选择此选项)
		 *  	-> 接收交易
		 * 			->将交易放入交易池里面
		 * 			->检查交易是否到达n个
		 * 				->组装成 merkle Tree 打包成block
		 * 		-> 挖block，在挖的过程中不断接收新的区块
		 * 		-> 如果发现接收到的区块验证通过并加到区块链里时，
		 * 			新的区块中有与(在正在被挖矿的区块中)相同的交易时
		 * 				-> 放弃挖矿，重新选择交易继续挖矿
		 * 		-> 如果块挖好了 将块打包发布
		 */
		
		
		/*
		 * 一个轻量级节点的功能
		 * 	->连接RPC
		 *  	-> 正常的查看区块链
		 *  	-> 转账
		 *  	-> 依托全节点验证区块
		 *  	-> 查看自己的余额->通过自己的公钥的哈希
		 */
		
		
		
		
		/*
		 * requirements
		 * -> 1. block 以及 transaction 的 toString 方法具体该怎么写 reason:  涉及到block的hash值计算问题
		 * -> 4. 组件代码测试所有函数正确性
		 * -> 5. 写一个手动区块链节点测试整体区块链运行状态
		 * -> 6. 完善RPC功能 开始写RPC手机客户端及桌面级程序
		 * -> 7. 讲服务功能封装成jar包 写commander
		 * 
		 * 
		 * 
		 * MyBerkeleyDB要不要设置成单例
		 */
	
	private static final int port = 8015;
	private static int bestHeight;
	private static PeerThread bestThread;
	
	public static void main(String[] args) throws NumberFormatException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("Starting daemon");
		System.out.println("Opening Database");
		Global.blockDB.open("Block");
		Global.blockDB.open("Config");  //Height of the current Block Chain
		Global.utxoDB.open("UTXO");

		
		
		//FIXME base58
		System.out.println("initiating user's keyPairs");
		KeyValuePairs keyValuePairs = new KeyValuePairs();
		Global.keyValuePairs = keyValuePairs;
		System.out.println("User's public key is :" + keyValuePairs.getPublicKey());
		System.out.println("User's private key is :" + keyValuePairs.getPrivateKey());
		
		Blockchain blockChain = new Blockchain();
		blockChain.newBlockchain();
		blockChain.addBlock(TestAddData.newBlock());
		UTXOSet.blockchain = blockChain;
		UTXOSet.Reindex();
		
		String userPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAoF4taDUDGujMDbn6v4He/fQ/AQ5n5492oxf05gsmiTNgA67yW+w3BjnKtc/HM1+YfUC+aUGtwkLRia1hEzSBr7iX77hh2kjiw8jUzWXfQ1s0jvDmxg+Ok7Kmha4hlf6AU4NrKg3EJ8DPgWI7N7iMq+IK4lpzZ18SSOZ+33KgAwIDAQAB";
		String payeePubKey1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQv5OxRvWpB7qJjzWZmlI+9Ggc2kpwG/vPneOv+DU+eTGNCEl8MKvmZGy+GqGwhFxhQpHHB3a8Gw+IMl2EijVJ9Q0wa3dbDiQ8p/LaUsLUi2BvMUUV8TC9e+YzPQI9uMm9j/Y9u6Y5VVEdv2GUdW9mFXxStn6OJBHJdYDX5+yMwIDAQAB";
		String payeePubKey2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrgxtZwKjrPGA9Vt0S7H579/CtCjQ0s5QzbVQSvW4Pia0kG+uggSH9CMSjtDzG1eaNlzf7ZBj/usAJAYpwEHwHq8hMv4eIywapoPhHsxMTzPi9wPNNAzpdhOgeKRBA1I3L9YJPZrxqbOpTaLrNxhD2XFJ28vKszuMSoROBsKpvIQIDAQAB";
		String payeePubKey3 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzDFj+IaxyMddAR70wdGEwAj/yFsFNKk1nD9I8ApYqPt+Eh+PQJSVSoffRjsyxONR/L5tJoqYl25wKh/4iTcAvNdkLnpYTsGs3bZXyuFxsrY120ipXngIaxa1MmbzBYriiXQgaWjtSS6hZe7Jg0Grn1+I0B5U4G5nyJZJ2+Tg8DwIDAQAB";
		String payeePrivate3 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALMMWP4hrHIx10BHvTB0YTACP/IWwU0qTWcP0jwClio+34SH49AlJVKh99GOzLE41H8vm0mipiXbnAqH/iJNwC812QuelhOwazdtlfK4XGytjXbSKleeAhrFrUyZvMFiuKJdCBpaO1JLqFl7smDQaufX4jQHlTgbmfIlknb5ODwPAgMBAAECgYBkf0zAL8RsQF9yXBHrzZbzbH/Z8T3Egzb43x+AwW7p/WpWKDQNk5v0WqcPv5hS7PQRA3alCR865p4aJyTUulFg/bd5gwMlVXJg/upRUYzmImUeCyWJ2h5hBcGfuDlB18KNheiUnkERMOcZf5cXt+NcheCxulTkWWMXgfKvFEY6kQJBAODHw1Kxhd4mr5044xJL6bJXG4NSsjykzz5apz9MmMAAeBQYj+ILth/JOxBvseD863+rSM+1s4x/2kuDc8uBERkCQQDL6ouHBZoAyTp+T30ezQdorKC/InAxPSYWGLaAj8gWnF/eZrA7a0bHvshpEjWSNyZrUQMfm8VLAiyx3fSpYZNnAkBX3l9BbToKfI774+gIF/rUB28u593bDQYitudYPEYeEFDgcjWUxMU+KbjYFQGxFM2ui7Ob1sjIbJZWHJ3geKTJAkEAlzIMTIwRuT17OoaTvk/Fi45cDfxp9YhiggXG5CI3+NPvnYbEavpK2/YQwR94SzbLLM0/pKMqMUimfSeWrjSkRwJALulj2nhX3DxhPusnJYFh6L9bLLMoxk2ZNY8F4FSZfDdXf2p1rwM36z8uIAairY3TaZFdQ5R82+nfJqIKhmFdQg==";
		System.out.println("user's balance:" + UTXOSet.getBalance(userPubKey));
		System.out.println("payee1's balance:" + UTXOSet.getBalance(payeePubKey1));
		System.out.println("payee2's balance:" + UTXOSet.getBalance(payeePubKey2));
		System.out.println("payee3's balance:" + UTXOSet.getBalance(payeePubKey3));
		
		
		//payee3 --21---> user
		//balance: user:71
		//payee1:3
		//payee2: 25
		//payee3: 21
		HashMap<String, HashSet<Vout>> spendable = UTXOSet.FindSpendableOutputs(payeePubKey3, 21);
		ArrayList<Vin> vins = new ArrayList<>();
		for(Entry<String, HashSet<Vout>> entry : spendable.entrySet()) {
			String txID = entry.getKey();
			for(Vout vout : entry.getValue()) {
				vins.add(new Vin(txID, vout.getSeqNum(), payeePubKey3));
			}	
		}
		Vin[] vinList = new Vin[vins.size()];
		
		for(int i=0;i<vinList.length;i++) {
			vinList[i] = vins.get(i);
		}
		Vout vout1 = new Vout(21, 0, Utilities.hashKeyForDisk(userPubKey));
		Vout vout2 = new Vout(1, 1, Utilities.hashKeyForDisk(payeePubKey3));
		Vout[] vouts = new Vout[2];
		vouts[0] = vout1;
		vouts[1] = vout2;
		Transaction t = new Transaction(vinList, vouts, false, payeePrivate3);
		ArrayList<Transaction> txs = new ArrayList<>();
		txs.add(t);
		BlockBody blockbody = new BlockBody(txs);
		Block block = new Block(blockbody, 23);
		blockChain.addBlock(block);
		UTXOSet.Update(blockbody);
		System.out.println("user's balance:" + UTXOSet.getBalance(userPubKey));
		System.out.println("payee1's balance:" + UTXOSet.getBalance(payeePubKey1));
		System.out.println("payee2's balance:" + UTXOSet.getBalance(payeePubKey2));
		System.out.println("payee3's balance:" + UTXOSet.getBalance(payeePubKey3));
		
		
		
// view two block
//		for(Block block: blockChain) {
//			System.out.println(block.getBlockBody().transactions.size());
//			for(Transaction t : block.getBlockBody().transactions) {
//				System.out.println(t.toString());
//			}
//			System.out.println("==========");
//		}
		
		//取出链高度
		System.out.print(blockChain.getHeight());
		//开启网络
		System.out.println("Starting peer network");
		PeerNetwork peerNetwork = new PeerNetwork(port);
		peerNetwork.start();
		System.out.println("Node Starts in port:"+port);
		
		System.out.println("Starting RPC daemon");
		RpcServer rpcAgent = new RpcServer(port+1);
		rpcAgent.start();
		System.out.println("RPC agent is Started in port:"+(port+1));
		
		ArrayList<String> peers = new ArrayList<String>();
		File peerFile = new File("peers.list");
		
		String host = NetworkUtils.getInternetIp();
		
		System.out.println("found that your computer has these following network Card");
		Map<String, String> hostList = Utilities.getInternetIp();
		for(String key:hostList.keySet()) {
			System.out.println("key: "+key+"   "+"ip"+hostList.get(key));
		}
		System.out.println("Please Choose one");
		Scanner scanner = new Scanner(System.in);
		host = hostList.get(scanner.nextLine());
		scanner.close();
		while(host == null) {
			System.out.println("found that your computer has these following network Card");
			hostList = Utilities.getInternetIp();
			for(String key:hostList.keySet()) {
				System.out.println("key: "+key+"   "+"ip"+hostList.get(key));
			}
			System.out.println("Please Choose one");
			scanner = new Scanner(System.in);
			host = hostList.get(scanner.nextLine());
			scanner.close();
		}
		
		
		if (!peerFile.exists()||FileUtils.readLines(peerFile,StandardCharsets.UTF_8).size()==0) {
			FileUtils.writeStringToFile(peerFile, host+":"+port,StandardCharsets.UTF_8,true);
		}else {
			for (String peer : FileUtils.readLines(peerFile,StandardCharsets.UTF_8)) {
				String[] addr = peer.split(":");
				if(NetworkUtils.isLocal(addr[0])&&String.valueOf(port).equals(addr[1])){
					continue;
				}
				peers.add(peer);
				//raw ipv4
				peerNetwork.connect(addr[0], Integer.parseInt(addr[1]));
			}
			if(!FileUtils.readLines(peerFile,StandardCharsets.UTF_8).contains(host+":"+port)) {
				System.out.println("Replace local Address");
				if(peers.size()>0) {
					peers.set(0, host+":"+port);
				}else {
					peers.add(host+":"+port);
				}
				FileUtils.writeStringToFile(peerFile, host+":"+port,StandardCharsets.UTF_8,false);
				for(int i=1;i<peers.size();i++) {
					FileUtils.writeStringToFile(peerFile, "\r\n"+peers.get(i),StandardCharsets.UTF_8,true);
				}
			}
		}
		
		for(Thread thread : peerNetwork.peerThreads) {
			while(thread.isAlive()) {
				//System.out.println("waiting for connection");
			}
		}
		System.out.println("broadcast ip address");
		peerNetwork.broadcast("ADDR " + host+":"+port);
		
		System.out.println("begin send broadcast");
		//建立socket连接后，给大家广播握手
		peerNetwork.broadcast("HEIGHT "+ blockChain.getHeight());
		
		while (true) {
			
			for(int i = 1;i<peers.size();i++) {
				if(!peerNetwork.peers.contains(peers.get(i))) {
					peers.clear();
					FileUtils.writeStringToFile(peerFile, host+":"+port,StandardCharsets.UTF_8,false);
				}
			}
			
			//对新连接过的peer写入文件，下次启动直接连接
			for (String peer : peerNetwork.peers) {
				if (!peers.contains(peer)) {
					peers.add(peer);
					FileUtils.writeStringToFile(peerFile, "\r\n"+peer,StandardCharsets.UTF_8,true);
				}
			}
			peerNetwork.peers.clear();
			PeerThread pt = null;
			// 处理通讯
			//for (PeerThread pt : peerNetwork.peerThreads) {
			for(int i = 0;i<peerNetwork.peerThreads.size();i++) {
				pt = peerNetwork.peerThreads.get(i);
				if (pt == null || pt.peerReader == null) {
					break;
				}
				List<String> dataList = pt.peerReader.readData();
				if (dataList == null) {
					System.out.println("Null return, retry.");
					System.exit(-5);
					break;
				}
				
				
				for (String data:dataList) {
					System.out.println("[p2p] COMMAND:: " + data);
					int flag = data.indexOf(' ');
					String cmd = flag >= 0 ? data.substring(0, flag) : data;
					String payload = flag >= 0 ? data.substring(flag + 1) : "";
					if (StringUtils.isNotBlank(cmd)) {
						if ("VERACK".equalsIgnoreCase(cmd)) {
							System.out.println("VERACK:"+payload);
							// 对方确认知道了,并给我区块高度
							bestHeight = Integer.parseInt(payload);
							if(bestHeight > blockChain.getHeight()) {
								bestThread = pt;
							}
						} else if ("HEIGHT".equalsIgnoreCase(cmd)) {
							System.out.println("HEIGHT:"+payload);
							// 对方发来握手信息
							// 获取区块高度和版本号信息
							int height = Integer.parseInt(payload);
							if(height >= blockChain.getHeight()) {
								bestHeight = height;
								bestThread = pt;
							}
							//我方回复：知道了
							//pt.peerWriter.write("VERACK " + blockChain.size() + " " + blockChain.get(blockChain.size() - 1).getHash());
							
							pt.peerWriter.write("VERACK "+blockChain.getHeight());
						} else if("GET_BLOCKS".equalsIgnoreCase(cmd)) {
							System.out.println("GET_BLOCKS:"+ payload);
							String hashList = Blockchain.generateInv(blockChain, payload);
							pt.peerWriter.write("INVENTORY "+ hashList);
						} else if("INVENTORY".equalsIgnoreCase(cmd)) {
							System.out.println("INVENTORY:"+ payload);
							String[] hashList = payload.split(",");
							for(int j=0;j<hashList.length;j++) {
								//在我们得到了INVENTORY以后，开始请求区块，发送GET_BLOCK命令
								pt.peerWriter.write("GET_BLOCK "+ hashList[j]);
								TimeUnit.MILLISECONDS.sleep(1000);
							}
						} else if ("GET_BLOCK".equalsIgnoreCase(cmd)) {
							System.out.println("GET_BLOCK:"+payload);
							//把对方请求的块给对方
							Block tempblock = blockChain.getBlock(payload);
							if (tempblock != null) {
								System.out.println("Sending block " + payload + " to peer");
								pt.peerWriter.write("BLOCK " + new String(Utilities.toByteArray(tempblock), "UTF-8"));
							}
						} else if ("BLOCK".equalsIgnoreCase(cmd)) {
							//把对方给的块存进链中
							System.out.println("Block:"+payload);
							System.out.println("Attempting to add Block: " + payload);
							Block newBlock = null;
							
							newBlock = (Block) Utilities.toObject(payload.getBytes("UTF-8"));
						
							if(blockChain.addBlock(newBlock)) {
								System.out.println("Added block " + payload + " with hash: ["+ newBlock.getHashCode() + "]");
								peerNetwork.broadcast("BLOCK " + payload);
							}
						} else if ("ADDR".equalsIgnoreCase(cmd)) {
							System.out.println("ADDR: "+payload);
							// 对方发来地址，建立连接并保存
							if (!peers.contains(payload)&&!(host+":"+port).equals(payload)) {
								String peerAddr = payload.substring(0, payload.indexOf(':'));
								int peerPort = Integer.parseInt(payload.substring(payload.indexOf(':') + 1));
								peerNetwork.connect(peerAddr, peerPort);
								peers.add(payload);
								PrintWriter out = new PrintWriter(peerFile);
								for (int k = 0; k < peers.size(); k++) {
									out.println(peers.get(k));
								}
								out.close();
							}
						} else if ("GET_ADDR".equalsIgnoreCase(cmd)) {
							//对方请求更多peer地址，随机给一个
							Random random = new Random();
							System.out.println(peers.size());
							pt.peerWriter.write("ADDR " + peers.get(random.nextInt(peers.size())));
						} 
					}
				}
			}
			
			// ********************************
			// 		比较区块高度,同步区块
			// ********************************
			int height = blockChain.getHeight();
			if(bestHeight > height) {
				System.out.println("Local chain height: " + height+" peer Height: " + bestHeight);
				TimeUnit.MILLISECONDS.sleep(300);
				bestThread.peerWriter.write("GET_BLOCKS "+ blockChain.tip);
			}

			// ********************************
			// 处理RPC服务
			// ********************************
			RpcThread rpcthread = null;
			for(int i=0;i<rpcAgent.rpcThreads.size();i++) {
				rpcthread = rpcAgent.rpcThreads.get(i);
				if(!rpcthread.isAlive()) {
					rpcAgent.rpcThreads.remove(i);
					continue;
				}
				String request = rpcthread.request;
				if (request != null) {
					String[] parts = request.split(" ");
					parts[0] = parts[0].toLowerCase();
					if ("getinfo".equals(parts[0])) {
						//String res = prettyGson.toJson(blockChain);
						String response = "block's Information";
						rpcthread.response = response;
					} else if ("send".equals(parts[0])) {
						try {
							int vac = Integer.parseInt(parts[1]);
							peerNetwork.broadcast("BLOCK "+"aBlock");
							/*
							// 根据vac创建区块
							Block newBlock = BlockUtils.generateBlock(blockChain.get(blockChain.size() - 1), vac);
							if (BlockUtils.isBlockValid(newBlock, blockChain.get(blockChain.size() - 1))) {
								blockChain.add(newBlock);
								rpcthread.response = "Block write Success!";
								FileUtils.writeStringToFile(dataFile,"\r\n"+gson.toJson(newBlock), StandardCharsets.UTF_8,true);
								//peerNetwork.broadcast("BLOCK " + gson.toJson(newBlock));
								peerNetwork.broadcast("BLOCK "+"aBlock");
							} else {
								rpcthread.response = "RPC 500: Invalid vac Error";
							}*/
						} catch (Exception e) {
							rpcthread.response = "Syntax (no '<' or '>'): send <vac> - Virtual Asset Count(Integer)";
							System.out.println("invalid vac - Virtual Asset Count(Integer)");
						}
					}else if("discoverip".equals(parts[0])) {
						peerNetwork.broadcast("GET_ADDR");
						rpcthread.response = "Request has been sent";
					}else {
						rpcthread.response = "Unknown command: \"" + parts[0] + "\" ";
					}
				}
			}
			// ****************
			// 循环结束
			// ****************
			TimeUnit.MILLISECONDS.sleep(100);
		}
		
	}

}
