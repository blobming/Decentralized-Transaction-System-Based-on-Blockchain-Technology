package Test;

import java.io.UnsupportedEncodingException;

import Utilities.Utilities;
import berkeleyDb.MyBerkeleyDB;
import obj.Block;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Block block = new Block("",155);
		System.out.println(block.getHashCode());
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
