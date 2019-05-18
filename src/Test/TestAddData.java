package Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.google.gson.Gson;

import Security.KeyValuePairs;
import Utilities.Utilities;
import config.Global;
import obj.Block;
import obj.BlockBody;
import obj.Blockchain;
import obj.TXPool;
import obj.Transaction;
import obj.UTXOSet;
import obj.Vin;
import obj.Vout;

public class TestAddData {
	public static String genesisTX = "1442cfe64c782b6077f8445b829be215a3bed269871f1fe3a7141ad81c0aa377";
	public static String userPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCryH4NG0hMI0P0uDaapwqgPvsIcaRvMMNzz9q5olmD1MVlIf7wT7SDDZITHFxjh607sZ3XfSGseLtKfwz+vrH5T6q4LlcvYwC3roVxp8OINzNKLKmRtVCpp/1AKqffO8aQGJcRmXvNlEkFWdBf5+fnHOtkmvxKH9oUIkA/kVydnwIDAQAB";
	public static String userPrivateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKvIfg0bSEwjQ/S4NpqnCqA++whxpG8ww3PP2rmiWYPUxWUh/vBPtIMNkhMcXGOHrTuxndd9Iax4u0p/DP6+sflPqrguVy9jALeuhXGnw4g3M0osqZG1UKmn/UAqp987xpAYlxGZe82USQVZ0F/n5+cc62Sa/Eof2hQiQD+RXJ2fAgMBAAECgYBJpZYtBlJTmhbrVwLKAO10UCwNrbMCnJRfFXC/f1QDSfgq6I1E99b7DJlgqhsN7MAfRIHYPeRu7f/V62xl1tmzxA4MpIBrguZ8PPxm92hCkq0Wvfk0kGxHRKoYL9vFA6re92KypDgjDC8/a3SFzABD3E1I8zVMMHUY6STzXTEtgQJBAPYbINT8rhQqtbWPkzRG793he39hvIn1/s0w3oFbESFjlhxy3PyjyDyM1j6KMEswHwwJgmH9hWRMxJLyxVpHyt8CQQCysHMEbTXhum+IEM+89UY9DzWGSznvPUJLQ833uv02raDwG8SpuFj7QLYcfIqqaV74m8nIGqCQIlKXdWFXDEVBAkEAvwFvCd7SfHRpcvBrnzcvE1XdM4/3dFun1jkHd/3l3bQc3ReIy8uRrOzhmshFkxVWA1GeD89jp+Ss7B/fa6IsoQJBAKdkqLrS3BEUJ/tdVF59n0kAAHJH8oPt7Ta41DBomWEpkoScXsVnjuQAoRlaikRN5cwblRHB7/xWSuzb5042joECQQDePjtSGsqIHr3lZZhDAOyDqg256UNvKKZ/Teh3stJ5RKMHnQDdX45UZheQrzetMuK5bHKC5ek4Y/cd7uCJ6AQ5";
	public static String payeePubKey1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQv5OxRvWpB7qJjzWZmlI+9Ggc2kpwG/vPneOv+DU+eTGNCEl8MKvmZGy+GqGwhFxhQpHHB3a8Gw+IMl2EijVJ9Q0wa3dbDiQ8p/LaUsLUi2BvMUUV8TC9e+YzPQI9uMm9j/Y9u6Y5VVEdv2GUdW9mFXxStn6OJBHJdYDX5+yMwIDAQAB";
	public static String payeePubKey2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrgxtZwKjrPGA9Vt0S7H579/CtCjQ0s5QzbVQSvW4Pia0kG+uggSH9CMSjtDzG1eaNlzf7ZBj/usAJAYpwEHwHq8hMv4eIywapoPhHsxMTzPi9wPNNAzpdhOgeKRBA1I3L9YJPZrxqbOpTaLrNxhD2XFJ28vKszuMSoROBsKpvIQIDAQAB";
	public static String payeePubKey3 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzDFj+IaxyMddAR70wdGEwAj/yFsFNKk1nD9I8ApYqPt+Eh+PQJSVSoffRjsyxONR/L5tJoqYl25wKh/4iTcAvNdkLnpYTsGs3bZXyuFxsrY120ipXngIaxa1MmbzBYriiXQgaWjtSS6hZe7Jg0Grn1+I0B5U4G5nyJZJ2+Tg8DwIDAQAB";
	public static String payeePrivate1 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIBC/k7FG9akHuomPNZmaUj70aBzaSnAb+8+d46/4NT55MY0ISXwwq+ZkbL4aobCEXGFCkccHdrwbD4gyXYSKNUn1DTBrd1sOJDyn8tpSwtSLYG8xRRXxML175jM9Aj24yb2P9j27pjlVUR2/YZR1b2YVfFK2fo4kEcl1gNfn7IzAgMBAAECgYA1D4H1REXH/g1sAHWanLNhpguKvTP8OcgUyVrcizSP5vFlrZso/vkLfBRyXaJ0LUTaGZeu40rWkC+3ZAjz9388V+rnFRoIWUibUu+zxwnIg6w9BjcnbBUjnNvrn6eJGM3npOe2k8nCLUQrsATbvTOwVZD005uba+bsvOB50/74QQJBAM//rkaK6tLHAtMpYI9AT78o8S99y/Gk353GnKzCX13rmY7mEjSl76leoGQOto5y+QP+yqi2UoqTi3i7geybVp8CQQCd3Iq2C8gKNqrLSLqOhsVrFq/z5VToYqq/RkoMONTgvO4kWqZlN5vMLpse8DYZM/UQNh40mrZ9ZNGykZELJN/tAkAqZ5TvGsku+ZWw2SGvcswPS9iH9xyKAjP4pZIxglsQ+cRPpsYVZ7MJk5odbAZ52iS8VMiaRrljORtZHNkrFKw/AkAwDA8gB4f0K91iEBnQBY8S4A3f0v472MQVgaRKAgYw/PASmpCon/tIcyr9iKk+lITTUVVyN2Cx27zDd/DIjomlAkEAkImNYS0Dx30Pfd6JysQAmiIgDbCo60ATtJC1rIlrcyVNMOHiGQGLaNgOEfFVUQvNPYLT9XCwaoPoTbGWejpxqQ==";
	public static String payeePrivate2 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKuDG1nAqOs8YD1W3RLsfnv38K0KNDSzlDNtVBK9bg+JrSQb66CBIf0IxKO0PMbV5o2XN/tkGP+6wAkBinAQfAeryEy/h4jLBqmg+EezExPM+L3A800DOl2E6B4pEEDUjcv1gk9mvGps6lNous3GEPZcUnby8qzO4xKhE4Gwqm8hAgMBAAECgYBVzp4cZTsdlXNAvPiVwyP0ubL7n6NGyqFWqVfqmEM8GtE4w487Gd/7TCu1CzNs9w8Fv6YOa6HmL3RsDQ1Jy7WA/wM1+maVdpB5nbIcMMdceBmwvkHDv1ZHZLS58QwPImBXpI/xx12BPwfD8li+VALyI5Z0Z2A1JnNWO4h1yoXzkQJBAN0ri0EGHVueH5Du+sggkdgEodGJMFInkMDSrBVY4UhUyp1zRo6nra87ZWXd03645TrxRcxFv9gqLZr0HTAZYzsCQQDGhZshRtzbY8pxopkxQGtJkpF9JvMtW1P4/PCcZgPlrBNTcJTrkx/FI73OK9SbuFXJlbj1y1co0BgIaaZAU5lTAkB3MvWtSNdfvlvy5+m/HFaeHvj7jj29uVc5pRFbu/hG3HQK4csALXzdOVzPxvsqjAn3d3uPuXPI1AqaPcDRYI1rAkBZ6I6Obpp93d8EwgAhVyx/4Jp1lNtTcmZAgMWz/1vKe7kw8+7uFe6wiPRgWGdssvpfVqiy4QmzWTNZNAfwYCwzAkEAriMCj2zZfeG79rkdDk9HjWFpuBp9ybkhVuNwxdpx7uAQKXBJIlthokjwHo1IdSi1Aw6wCHAlGrVFmOe9gzws1Q==";
	public static String payeePrivate3 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALMMWP4hrHIx10BHvTB0YTACP/IWwU0qTWcP0jwClio+34SH49AlJVKh99GOzLE41H8vm0mipiXbnAqH/iJNwC812QuelhOwazdtlfK4XGytjXbSKleeAhrFrUyZvMFiuKJdCBpaO1JLqFl7smDQaufX4jQHlTgbmfIlknb5ODwPAgMBAAECgYBkf0zAL8RsQF9yXBHrzZbzbH/Z8T3Egzb43x+AwW7p/WpWKDQNk5v0WqcPv5hS7PQRA3alCR865p4aJyTUulFg/bd5gwMlVXJg/upRUYzmImUeCyWJ2h5hBcGfuDlB18KNheiUnkERMOcZf5cXt+NcheCxulTkWWMXgfKvFEY6kQJBAODHw1Kxhd4mr5044xJL6bJXG4NSsjykzz5apz9MmMAAeBQYj+ILth/JOxBvseD863+rSM+1s4x/2kuDc8uBERkCQQDL6ouHBZoAyTp+T30ezQdorKC/InAxPSYWGLaAj8gWnF/eZrA7a0bHvshpEjWSNyZrUQMfm8VLAiyx3fSpYZNnAkBX3l9BbToKfI774+gIF/rUB28u593bDQYitudYPEYeEFDgcjWUxMU+KbjYFQGxFM2ui7Ob1sjIbJZWHJ3geKTJAkEAlzIMTIwRuT17OoaTvk/Fi45cDfxp9YhiggXG5CI3+NPvnYbEavpK2/YQwR94SzbLLM0/pKMqMUimfSeWrjSkRwJALulj2nhX3DxhPusnJYFh6L9bLLMoxk2ZNY8F4FSZfDdXf2p1rwM36z8uIAairY3TaZFdQ5R82+nfJqIKhmFdQg==";
	public static Block newBlock() {
		/////////////////////////////////////////////////////////////
		//User---20--->payeePubKey1
		//User---30--->payeePubKey2
		//User's balance: 50
		//payeePubKey1's balance: 20
		//payeePubKey2's balance: 30
		Vin vin = new Vin(Global.genesisTX, 0, Global.keyValuePairs.getPublicKey());
		Vout vout1 = new Vout(20, 0, Utilities.hashKeyForDisk(payeePubKey1));
		Vout vout2 = new Vout(30, 1, Utilities.hashKeyForDisk(payeePubKey2));
		//剩下的钱返还给payer
		Vout vout3 = new Vout(50, 2, Utilities.hashKeyForDisk(Global.keyValuePairs.getPublicKey()));
		Vin[] vins1 = new Vin[1];
		vins1[0] = vin;
		Vout[] vouts1 = new Vout[3];
		vouts1[0] = vout1;
		vouts1[1] = vout2;
		vouts1[2] = vout3;
		Transaction t1 = new Transaction(vins1, vouts1, false, Global.keyValuePairs.getPrivateKey(),new Date());
		///////////////////////////////////////////////////
		//payeePubKey2--5-->payeePubKey1
		//User's balance: 50
		//payeePubKey1's balance: 25
		//payeePubKey2's balance: 25
		Vin vin2  = new Vin(t1.getTxid(), 1, payeePubKey2);
		Vout vout4 = new Vout(5, 0, Utilities.hashKeyForDisk(payeePubKey1));
		Vout vout5 = new Vout(25, 1, Utilities.hashKeyForDisk(payeePubKey2));
		Vin[] vins2 = new Vin[1];
		vins2[0] = vin2;
		Vout[] vouts2 = new Vout[2];
		vouts2[0] = vout4;
		vouts2[1] = vout5;
		Transaction t2 = new Transaction(vins2, vouts2, false, payeePrivate2, new Date());
		//////////////////////////////////////////////////////
		//payeePubKey1--22-->payeePubKey3
		//User's balance: 50
		//payeePubKey1's balance: 3
		//payeePubKey2's balance: 25
		//payeePubKey3's balance: 22
		Vin vin3 = new Vin(t1.getTxid(), 0, payeePubKey1);
		Vin vin4 = new Vin(t2.getTxid(), 0, payeePubKey1);
		Vout vout6 = new Vout(22, 0, Utilities.hashKeyForDisk(payeePubKey3));
		Vout vout7 = new Vout(3, 1, Utilities.hashKeyForDisk(payeePubKey1));
		Vin[] vins3 = new Vin[2];
		vins3[0] = vin3;
		vins3[1] = vin4;
		Vout[] vouts3 = new Vout[2];
		vouts3[0] = vout6;
		vouts3[1] = vout7;
		Transaction t3 = new Transaction(vins3, vouts3, false, payeePrivate1, new Date());
		ArrayList<Transaction> txs = new ArrayList<>();
		txs.add(t1);
		txs.add(t2);
		txs.add(t3);
		BlockBody body = new BlockBody(txs);
		//body.browseWholeTree();
		Block block = new Block(body, 123, UTXOSet.blockchain.tip, new Date());
		return block;
	}
	public static void InitUserInfo() {
		//FIXME base58
		System.out.println("initiating user's keyPairs");
		KeyValuePairs keyValuePairs = new KeyValuePairs();
		keyValuePairs.setPrivateKey(userPrivateKey);
		keyValuePairs.setPublicKey(userPubKey);
		Global.keyValuePairs = keyValuePairs;
		System.out.println("User's public key is :" + keyValuePairs.getPublicKey());
		System.out.println("User's private key is :" + keyValuePairs.getPrivateKey());
		System.out.println("user's balance:" + UTXOSet.getBalance(userPubKey));
		System.out.println("payee1's balance:" + UTXOSet.getBalance(payeePubKey1));
		System.out.println("payee2's balance:" + UTXOSet.getBalance(payeePubKey2));
		System.out.println("payee3's balance:" + UTXOSet.getBalance(payeePubKey3));
	}
	public static void InitBlock() {
		UTXOSet.Reindex();
		UTXOSet.blockchain.addBlock(TestAddData.newBlock());
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
		Transaction t = new Transaction(vinList, vouts, false, payeePrivate3, new Date());
		//ArrayList<Transaction> txs = new ArrayList<>();
		TXPool.putInPool(t);
		//txs.add(t);
		//BlockBody blockbody = new BlockBody(txs);
		//Block block = new Block(blockbody, 23, UTXOSet.blockchain.tip, new Date());
		//UTXOSet.blockchain.addBlock(block);
		//UTXOSet.Update(blockbody);
		
	}
	
}
