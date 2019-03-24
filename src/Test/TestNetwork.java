package Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import Network.*;

public class TestNetwork {
	
	private static final int port = 8015;
	private static final String VERSION = "0.1";

	public static void main(String[] args) throws IOException, InterruptedException {
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
		String host = CommonUtils.getInternetIp();
		if (!peerFile.exists()) {
			FileUtils.writeStringToFile(peerFile, host+":"+port,StandardCharsets.UTF_8,true);
		}else {
			for (String peer : FileUtils.readLines(peerFile,StandardCharsets.UTF_8)) {
				String[] addr = peer.split(":");
				if(CommonUtils.isLocal(addr[0])&&String.valueOf(port).equals(addr[1])){
					continue;
				}
				peers.add(peer);
				//raw ipv4
				peerNetwork.connect(addr[0], Integer.parseInt(addr[1]));
			}
			if(!FileUtils.readLines(peerFile,StandardCharsets.UTF_8).contains(host+":"+port)) {
				System.out.println("Replace local Address");
				peers.set(0, host+":"+port);
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
		//System.out.println("peers: "+peers.size());
		//System.out.println("begin send broadcast");
		int bestHeight = 2;
		//建立socket连接后，给大家广播握手
		peerNetwork.broadcast("VERSION "+ bestHeight+" " + VERSION);
		//System.out.println("send broadcast success");
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
							//String[] parts = payload.split(" ");
							//bestHeight = Integer.parseInt(parts[0]);
							//哈希暂时不校验
						} else if ("VERSION".equalsIgnoreCase(cmd)) {
							System.out.println("VERSION:"+payload);
							// 对方发来握手信息
							// 获取区块高度和版本号信息
							String[] parts = payload.split(" ");
							bestHeight = Integer.parseInt(parts[0]);
							System.out.println("Recording IP Address");
							//我方回复：知道了
							//pt.peerWriter.write("VERACK " + blockChain.size() + " " + blockChain.get(blockChain.size() - 1).getHash());
							pt.peerWriter.write("VERACK "+"2");
						} else if ("BLOCK".equalsIgnoreCase(cmd)) {
							//把对方给的块存进链中
							System.out.println("Block:"+payload);
							peerNetwork.broadcast("BLOCK "+payload);
							/*
							Block newBlock = gson.fromJson(payload, Block.class);
							if (!blockChain.contains(newBlock)) {
								LOGGER.info("Attempting to add Block: " + payload);
								// 校验区块，如果成功，将其写入本地区块链
								if (BlockUtils.isBlockValid(newBlock, blockChain.get(blockChain.size() - 1))) {
									blockChain.add(newBlock);
									LOGGER.info("Added block " + newBlock.getIndex() + " with hash: ["+ newBlock.getHash() + "]");
									FileUtils.writeStringToFile(dataFile,"\r\n"+gson.toJson(newBlock), StandardCharsets.UTF_8,true);
									peerNetwork.broadcast("BLOCK " + payload);
								}
							}*/
						} else if ("GET_BLOCK".equalsIgnoreCase(cmd)) {
							System.out.println("GET_BLOCK:"+payload);
							System.out.println("Sending block " + payload + " to peer");
							pt.peerWriter.write("BLOCK " + "aBlock");
							//把对方请求的块给对方
							/*
							Block block = blockChain.get(Integer.parseInt(payload));
							if (block != null) {
								LOGGER.info("Sending block " + payload + " to peer");
								pt.peerWriter.write("BLOCK " + gson.toJson(block));
							}*/
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
			/*
			int localHeight = blockChain.size();
			if (bestHeight > localHeight) {
				LOGGER.info("Local chain height: " + localHeight+" Best chain Height: " + bestHeight);
				TimeUnit.MILLISECONDS.sleep(300);
				
				for (int i = localHeight; i < bestHeight; i++) {
					LOGGER.info("request get block[" + i + "]...");
					peerNetwork.broadcast("GET_BLOCK " + i);
				}
			}*/

			// ********************************
			// 处理RPC服务
			// ********************************
			for (RpcThread rpcthread:rpcAgent.rpcThreads) {
				if(!rpcthread.isAlive()) {
					continue;
				}
				String request = rpcthread.request;
				//System.out.println(request);
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

