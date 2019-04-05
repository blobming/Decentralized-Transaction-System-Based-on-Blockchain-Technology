package Test;

import java.util.ArrayList;

import Utilities.Utilities;
import obj.Block;
import obj.BlockBody;
import obj.Transaction;
import obj.Vin;
import obj.Vout;

public class TestAddData {
	public static Block newBlock() {
		String genesisTX = "11626ab12711bc28d6f426ddb9f0d737250b67d68ca15e39621c98a7e3340084";
		String userPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCr3uHYP8s/nNQY+8GeHedSyClJcTiDvpdZTGuLDy/NpjEUNK9rYyEcnzNWDUvvlRRVvnqm2Zwt7HWfkZmaYfQ1GocBbyPofW5tfVJBmtldychafqAta9Acr3ElCWnmUdv4WmWy9ByX6L6uhl1tJyN6FFa20UoIq1x1fRS6XWccuQIDAQAB";
		String payeePubKey1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCS2hqpbQYShmJloGR5PeZdU2xqdSq6Pl6CH4UVFnKZ1Tzmkrv4M23nOXLJktu6GkzIwfzRVMSoBjfk9tP99Stjc6/5CPR3JAD52R3PfZnGiDUcher0extbuzZGMwHIoeOUbMW4auhfpElgHDvqyyxbqJ7bpqzBHqaWhoO7t7jpdQIDAQAB";
		String payeePubKey2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAClG15J6QIEMdaxH+CJEKduGtoQokw3hUsd2nQ5XdjNog/tqEI/MnVAJYGRXQZz4slctIf8ZTwplhykcnH4vfv45DkyY37bbjjtuVXWUCB5OP6dmFxJ8/W69B5l4I4Nuq8TvfCZNf9jPwI3jXrq1jGiZnkxUHrQ5Eeh8SLnO/+wIDAQAB";
		String payeePubKey3 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQ2E2uH0DjLcEnxgK6TOAxG24ghP0RePxTFuUNZJgUpFFcBolk3iQ2a+I51XU948uuOuubH5m3HhsnPh2ldXSwYP5ECg6qMF6NWfB5G37NHyiYfKBgdDkA8GtRDlhmjWgxynHTWFTX2BlZpb7HgbO/LEhTz+euOERMbJWL+/JDhQIDAQAB";
		/////////////////////////////////////////////////////////////
		//User---20--->payeePubKey1
		//User---30--->payeePubKey2
		//User's balance: 50
		//payeePubKey1's balance: 20
		//payeePubKey2's balance: 30
		Vin vin = new Vin(genesisTX, 0, userPubKey);
		Vout vout1 = new Vout(20, 0, Utilities.hashKeyForDisk(payeePubKey1));
		Vout vout2 = new Vout(50, 1, Utilities.hashKeyForDisk(payeePubKey2));
		//剩下的钱返还给payer
		Vout vout3 = new Vout(30, 2, Utilities.hashKeyForDisk(userPubKey));
		Vin[] vins1 = new Vin[1];
		vins1[0] = vin;
		Vout[] vouts1 = new Vout[3];
		vouts1[0] = vout1;
		vouts1[1] = vout2;
		vouts1[2] = vout3;
		Transaction t1 = new Transaction(vins1, vouts1);
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
		Transaction t2 = new Transaction(vins2, vouts2);
		//////////////////////////////////////////////////////
		//payeePubKey1--22-->payeePubKey3
		//User's balance: 50
		//payeePubKey1's balance: 23
		//payeePubKey2's balance: 25
		//payeePubKey3's balance: 22
		Vin vin3 = new Vin(t1.getTxid(), 0, payeePubKey1);
		Vin vin4 = new Vin(t2.getTxid(), 0, payeePubKey1);
		Vout vout6 = new Vout(22, 0, Utilities.hashKeyForDisk(payeePubKey3));
		Vout vout7 = new Vout(23, 1, Utilities.hashKeyForDisk(payeePubKey1));
		Vin[] vins3 = new Vin[2];
		vins3[0] = vin3;
		vins3[1] = vin4;
		Vout[] vouts3 = new Vout[2];
		vouts3[0] = vout6;
		vouts3[1] = vout7;
		Transaction t3 = new Transaction(vins3, vouts3);
		ArrayList<Transaction> txs = new ArrayList<>();
		txs.add(t1);
		txs.add(t2);
		txs.add(t3);
		System.out.println("c");
		BlockBody body = new BlockBody(txs);
		System.out.println("d");
		Block block = new Block(body, 123);
		System.out.println("newBlock:" + block.getHashCode());
		return block;
	}
	public static void main(String[] args) {
		
		
	}
	
}
