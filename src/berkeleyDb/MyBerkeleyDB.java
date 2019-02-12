package berkeleyDb;
import java.io.File;
import java.util.ArrayList;

import com.sleepycat.je.*;

public class MyBerkeleyDB {
	 private Environment environment;
	 private Database database;    
	 private String path;
	 private String charset;
	 public MyBerkeleyDB() {
		 charset = "utf-8";
	 }
	 public void setPath(String path) {
		//判断Path是否存在 
		File file = new File(path);
		if(file.mkdir()){ 
			System.out.println(path+" has been created"); //不存在则创建一个
		}else{
			System.out.println(path+" exist!"); //存在则说明已存在 
		} 
		//确定存储路径 
		this.path = path;
	 }
	 public void setEnvironment(String path , long chacheSize){
		 setPath(path); 
		 //setChacheSize(chacheSize); 
		 //配置环境
		 EnvironmentConfig envConfig = new EnvironmentConfig(); 
		 envConfig.setAllowCreate(true); 
		 //envConfig.setCacheSize(this.chacheSize); 
		 //创建环境 
		 environment = new Environment(new File(this.path),envConfig); 
	 }
	 //open database
	 public void open(String dbName) { 
		 DatabaseConfig dbConfig = new DatabaseConfig(); 
		 dbConfig.setAllowCreate(true); 
		 dbConfig.setSortedDuplicates(false); //不存储重复关键字 
		 this.database = environment.openDatabase(null, dbName, dbConfig); 
	 }
	 public void close()
	 {
        database.close();
        environment.close();
	 }
	 public void put(String key,String value) {
		 try {
			 DatabaseEntry k = new DatabaseEntry(key.getBytes(charset)); 
			 DatabaseEntry v = new DatabaseEntry(value.getBytes(charset)); 
			 database.put(null, k, v); 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	 }
	 public String get(String key) { 

		 DatabaseEntry k = new DatabaseEntry(key.getBytes(charset)); 
		 DatabaseEntry v = new DatabaseEntry(); 
		 if(database.get(null, k, v, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			 byte[] retData = v.getData();
			 String res = new String(retData,charset);
		 }else {
			 return null;
		 }
	 }
	 
	 public ArrayList<String> getAllKey() { 
		 ArrayList<String> result = new ArrayList<String>(); 
		 Cursor cursor = database.openCursor(null, null); 
		 DatabaseEntry key = new DatabaseEntry(); 
		 DatabaseEntry value = new DatabaseEntry(); 
		 while(cursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) { 
			 byte[] bytes = key.getData(); 
			 if(bytes != null) { 
				 result.add(new String(bytes)); 
				 key = new DatabaseEntry(); 
			 }else{
				 key = new DatabaseEntry(); 
			 }
		 } 
		 cursor.close(); 
		 return result; 
	 }
	 
	
	

}
