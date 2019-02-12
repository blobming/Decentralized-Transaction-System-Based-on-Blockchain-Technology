package berkeleyDb;
import java.io.File;

import com.sleepycat.je.*;

public class Test {
	private static Environment environment;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//鍚孍nvironmentConfig鏉ラ厤缃幆澧�
	    EnvironmentConfig environmentConfig=new EnvironmentConfig();
	    environmentConfig.setTransactional(true);
	    environmentConfig.setAllowCreate(true);
	    //homeDirectory鏄暟鎹簱瀛樻斁鐨勭洰褰�
	    
	//    environmentConfig = null;
	  //Open Environment
	    environment=new Environment(new File("./Data"),environmentConfig);
	    
	    String dbName = "test";
	    DatabaseConfig dbconfig  = new DatabaseConfig();
	    dbconfig.setAllowCreate(true);
	    dbconfig.setTransactional(false);
	    dbconfig.setSortedDuplicates(false);
	    Database myClassDb = environment.openDatabase(null, "classDb", dbconfig);
	    String key = "key1";
	    String value = "value1";
	   try {
		   //设置key/value,注意DatabaseEntry内使用的是bytes数组
		   DatabaseEntry theKey=new DatabaseEntry(key.getBytes("UTF-8"));
		   DatabaseEntry theData=new DatabaseEntry(value.getBytes("UTF-8"));
		   myClassDb.put(null, theKey, theData);
		  } catch (Exception e) {
		      // 错误处理
		  }
		  
		 
	    
	    
	    
	}

}
