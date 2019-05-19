package userInterface;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.google.gson.Gson;
import Network.PeerNetwork;
import Network.PeerThread;
import Network.RpcServer;
import Network.RpcThread;
import Security.KeyValuePairs;
import Test.TestAddData;
import Utilities.NetworkUtils;
import Utilities.Utilities;
import config.Global;
import obj.*;

public class BlockChainEntrance extends Thread {
	private final int port = 8015;
	private int bestHeight;
	private PeerThread bestThread;
	private String networkCard;
	
	public BlockChainEntrance(String networkCard) {
		this.networkCard = networkCard;
	}
	
	@Override
	public void run() {
		try {
			this.MainThread();
		} catch (NumberFormatException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void MainThread() throws NumberFormatException, IOException, InterruptedException {
		System.out.println("Starting daemon");
		System.out.println("Opening Database");
		Global.blockDB.open("Block");
		Global.blockDB.open("Config");  //Height of the current Block Chain
		Global.utxoDB.open("UTXO");
		Global.txDB.open("TXPool");
		
		Blockchain blockChain = new Blockchain();
		UTXOSet.blockchain = blockChain;
		TestAddData.InitUserInfo();
		UTXOSet.blockchain.newBlockchain();
		UTXOSet.Reindex();
		ArrayList<Block> blockList = new ArrayList<Block>();
		for(Block block: UTXOSet.blockchain) {
			blockList.add(block);
		}
		System.out.println(new Gson().toJson(blockList));
		TestAddData.InitBlock();
		
		System.out.println("当前用户余额");
		System.out.println("user's balance:" + UTXOSet.getBalance(TestAddData.userPubKey));
		System.out.println("payee1's balance:" + UTXOSet.getBalance(TestAddData.payeePubKey1));
		System.out.println("payee2's balance:" + UTXOSet.getBalance(TestAddData.payeePubKey2));
		System.out.println("payee3's balance:" + UTXOSet.getBalance(TestAddData.payeePubKey3));
		
		//view two block
		blockList = new ArrayList<Block>();
		for(Block block: UTXOSet.blockchain) {
			System.err.println(block.getHashCode());
			blockList.add(block);
		}
		System.err.println(new Gson().toJson(blockList));
		
		//取出链高度
		System.out.println("当前区块链高度：" + UTXOSet.blockchain.getHeight());
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
		
		Map<String, String> hostList = Utilities.getInternetIp();
		String host = hostList.get(networkCard);
		
		//如果peer.txt未创建，则创建一个并将自己的ip地址写入peer.txt
		if (!peerFile.exists()||FileUtils.readLines(peerFile,StandardCharsets.UTF_8).size()==0) {
			FileUtils.writeStringToFile(peerFile, host+":"+port,StandardCharsets.UTF_8,true);
		}else { //如果peer.txt已经存在，就把它存的ip地址放入peers里面
			for (String peer : FileUtils.readLines(peerFile,StandardCharsets.UTF_8)) {
				String[] addr = peer.split(":");
				if(NetworkUtils.isLocal(addr[0])&&String.valueOf(port).equals(addr[1])){
					continue;
				}
				peers.add(peer);
				//raw ipv4
				//尝试连接
				peerNetwork.connect(addr[0], Integer.parseInt(addr[1]));
			}
			//如果peerlist的第一条不是自己的ip地址(当前自己的ip地址改变）
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
		//临时补救
		for(PeerThread pt : peerNetwork.peerThreads) {
			while(pt.peerReader == null || pt.peerWriter == null) {
				System.out.println("waiting for connecting");
			}
		}
		
		
		System.out.println("broadcast ip address");
		peerNetwork.broadcast("ADDR " + host+":"+port);
		
		System.out.println("begin send broadcast");
		//建立socket连接后，给大家广播握手
		peerNetwork.broadcast("HEIGHT "+ blockChain.getHeight());
		
		while (true) {
			//对新连接的peer写入文件
			for (String peer : peerNetwork.peers) {
				if (!peers.contains(peer)) {
					peers.add(peer);
					FileUtils.writeStringToFile(peerFile, "\r\n"+peer,StandardCharsets.UTF_8,true);
				}
			}
			peerNetwork.peers.clear();
			PeerThread pt = null;
			
			//处理各个线程
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
					System.out.println("COMMAND: " + data);
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
							int height = Integer.parseInt(payload);
							if(height >= blockChain.getHeight()) {
								bestHeight = height;
								bestThread = pt;
							}
							pt.peerWriter.write("VERACK "+blockChain.getHeight());
						} else if("GET_BLOCKS".equalsIgnoreCase(cmd)) {
							System.out.println("GET_BLOCKS:"+ payload);
							String hashList = Blockchain.generateInv(blockChain, payload);
							pt.peerWriter.write("BLOCK_INV "+ hashList);
						} else if("BLOCK_INV".equalsIgnoreCase(cmd)) {
							System.out.println("BLOCK_INV:"+ payload);
							String[] hashList = payload.split(",");
							for(int j=0;j<hashList.length;j++) {
								//在我们得到了INVENTORY以后，开始请求区块，发送GET_BLOCK命令
								pt.peerWriter.write("GET_BLOCK "+ hashList[j]);
								TimeUnit.MILLISECONDS.sleep(1000);
							}
						} else if ("GET_BLOCK".equalsIgnoreCase(cmd)) {
							System.out.println("GET_BLOCK:"+payload);
							Block tempblock = blockChain.getBlock(payload);
							if (tempblock != null) {
								System.out.println("Sending block " + payload + " to peer");
								pt.peerWriter.write("BLOCK " + Base64.getEncoder().encodeToString(Utilities.toByteArray(tempblock)));
							}
						} else if ("BLOCK".equalsIgnoreCase(cmd)) {
							System.out.println("Block:"+payload);
							System.out.println("Attempting to add Block: " + payload);
							Block newBlock = (Block) Utilities.toObject(Base64.getDecoder().decode(payload));
							if(blockChain.addBlock(newBlock)) {
								System.out.println("Added block " + payload + " with hash: ["+ newBlock.getHashCode() + "]");
								peerNetwork.broadcast("BLOCK " + payload);
							}
						} else if ("ADDR".equalsIgnoreCase(cmd)) {
							System.out.println("ADDR: "+payload);
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
							Random random = new Random();
							System.out.println(peers.size());
							pt.peerWriter.write("ADDR " + peers.get(random.nextInt(peers.size())));
						} else if("SYNC_TRANSACTION".equalsIgnoreCase(cmd)) {
							pt.peerWriter.write("TRANSACTION_INV " + Base64.getEncoder().encodeToString(Utilities.toByteArray(TXPool.getAllHash())));
						} else if("TRANSACTION_INV".equalsIgnoreCase(cmd)) {
							ArrayList<String> transactionInv = (ArrayList<String>) Utilities.toObject(Base64.getDecoder().decode(payload));
							for(String t : transactionInv) {
								if(!TXPool.contains(t)) {
									pt.peerWriter.write("GET_TRANSACTION " + t);
								}
							}
						} else if("GET_TRANSACTION".equalsIgnoreCase(cmd)) {
							if(TXPool.contains(payload)) {
								pt.peerWriter.write("TRANSACTION " + Base64.getEncoder().encodeToString(Utilities.toByteArray(TXPool.get(payload))));
							}
						} else if("TRANSACTION".equalsIgnoreCase(cmd)) {
							Transaction transaction = (Transaction) Utilities.toObject(Base64.getDecoder().decode(payload));
							if(transaction != null) {
								if(!TXPool.contains(transaction)) {
									TXPool.putInPool(transaction);
									peerNetwork.broadcast("TRANSACTION " + payload);
								}
							}
						}
					}
				}
			}

			
			int height = blockChain.getHeight();
			if(bestHeight > height) {
				System.out.println("Local chain height: " + height+" peer Height: " + bestHeight);
				TimeUnit.MILLISECONDS.sleep(300);
				bestThread.peerWriter.write("GET_BLOCKS "+ blockChain.tip);
			}

			
			RpcThread rpcthread = null;
			for(int i=0;i<rpcAgent.rpcThreads.size();i++) {
				rpcthread = rpcAgent.rpcThreads.get(i);
				if(!rpcthread.isAlive()) {
					rpcAgent.rpcThreads.remove(i);
					System.out.println("An client has been disconnected");
					continue;
				}
				String request = rpcthread.request;
				if (request != null) {
					System.out.println("COMMAND: " + request);
					int flag = request.indexOf(' ');
					String cmd = flag >= 0 ? request.substring(0, flag) : request;
					String payload = flag >= 0 ? request.substring(flag + 1) : "";
					cmd = cmd.toUpperCase();
					System.out.println(cmd);
					System.out.println(payload);
					if ("GET_INFO".equals(cmd)) {
						ArrayList<Block> blockList1 = new ArrayList<Block>();
						for(Block block: UTXOSet.blockchain) {
							System.err.println(block.getHashCode());
							blockList1.add(block);
						}
						String jsonString = new Gson().toJson(blockList1);
						rpcthread.response = new Gson().toJson(new Status("1", jsonString))+"_FIN";
					}else if("SYNC_TRANSACTION".equals(cmd)) {
						peerNetwork.broadcast("SYNC_TRANSACTION");
						rpcthread.response = new Gson().toJson(new Status("1", "Request has been sent"))+"_FIN";
					}else if("DISCOVER_IP".equals(cmd)) {
						peerNetwork.broadcast("GET_ADDR");
						rpcthread.response = new Gson().toJson(new Status("1", "Request has been sent"))+"_FIN";
					}else if("TRANSACTION".equals(cmd)) {
						Transaction transaction = new Gson().fromJson(payload, Transaction.class);
						peerNetwork.broadcast("TRANSACTION " + Base64.getEncoder().encodeToString(Utilities.toByteArray(transaction)));
						rpcthread.response = new Gson().toJson(new Status("1", "Request has been sent"))+"_FIN";
					}else if("BALANCE".equals(cmd)) {
						Double balance = UTXOSet.getBalance(payload);
						rpcthread.response = new Gson().toJson(new Status("1", balance.toString()))+"_FIN";
					}else {
						rpcthread.response = new Gson().toJson(new Status("0", "Unknown command: \"" + cmd + "\" "))+"_FIN";
					}
				}
			}
			TimeUnit.MILLISECONDS.sleep(100);
		}
		
	}

}
