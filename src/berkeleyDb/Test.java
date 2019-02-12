package berkeleyDb;
import java.io.File;

import com.sleepycat.je.*;

public class Test {
	private static Environment environment;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		
	    EnvironmentConfig environmentConfig=new EnvironmentConfig();
	    environmentConfig.setTransactional(true);
	    environmentConfig.setAllowCreate(true); //如果不存在则创建一个
	    
	//    environmentConfig = null;
	  //Open Environment
	    environment=new Environment(new File("./Data"),environmentConfig);
	    
	    String dbName = "test";
	    */
		Environment env =  BerkeleyDb.GetInstance().GetEnvironment();
	    Database myClassDb = env.openDatabase(null, "classDb", BerkeleyDb.GetDbConfig());
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
