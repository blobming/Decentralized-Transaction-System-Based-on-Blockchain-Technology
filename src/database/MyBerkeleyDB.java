package database;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sleepycat.je.*;

import utilities.Utilities;

public class MyBerkeleyDB {
	private Environment environment;
	private Database database;    
	private String path;
	private String charset;
	
	public MyBerkeleyDB(String path) {
		setEnvironment(path);
		charset = "UTF-8";
		
	}
	private void setPath(String p) {
		//判断path是否存在
		File file = new File(p);
		if(file.mkdir()){ 
			System.out.println(p+" has been created"); //不存在就创建一个
		}else{
			System.out.println(p+" exist!"); //存在则说明已存在
		} 
		//确定存储路径
		path = p;
	}
	private void setEnvironment(String p){
		setPath(p); 
		EnvironmentConfig envConfig = new EnvironmentConfig(); 
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true); 
		//envConfig.setCacheSize(this.chacheSize); 
		//创建环境
		environment = new Environment(new File(p),envConfig); 
	}
	
	public Environment getEnvironment() {
		return environment;
	}

	//open database
	public void open(String dbName) { 
		DatabaseConfig dbConfig = new DatabaseConfig(); 
		dbConfig.setAllowCreate(true); 
		dbConfig.setTransactional(true);
		dbConfig.setSortedDuplicates(false); //不存储重复关键字
		database = environment.openDatabase(null, dbName, dbConfig); 
	}
	public void closeEnvironment()
	{
	   environment.close();
	}
	public void closeDatabase() {
		database.close();
	}
	public void put(Object key, Object value) {
		try {
			if(Utilities.toByteArray(key) == null)	System.out.println("null key");
			if(Utilities.toByteArray(value) == null)	System.out.println("null value");
			DatabaseEntry k = new DatabaseEntry(Utilities.toByteArray(key)); 
			DatabaseEntry v = new DatabaseEntry(Utilities.toByteArray(value));
			database.put(null, k, v);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
//	public void put(String key,String value) {
//		try {
//			DatabaseEntry k = new DatabaseEntry(key.getBytes(charset)); 
//			System.out.println("put key:" +key.getBytes(charset));
//			DatabaseEntry v = new DatabaseEntry(value.getBytes(charset));
//			System.out.println("put value:" +value.getBytes(charset));
//			database.put(null, k, v); 
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void put(byte[] key, byte[] value) {
//		try {
//			DatabaseEntry k = new DatabaseEntry(key); 
//			DatabaseEntry v = new DatabaseEntry(value); 
//			database.put(null, k, v); 
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public void put(String key, byte[] value) {
//		try {
//			DatabaseEntry k = new DatabaseEntry(key.getBytes(charset));
//			DatabaseEntry v = new DatabaseEntry(value); 
//			database.put(null, k, v); 
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public void put(byte[] key, String value) {
//		try {
//			DatabaseEntry k = new DatabaseEntry(key); 
//			DatabaseEntry v = new DatabaseEntry(value.getBytes(charset)); 
//			database.put(null, k, v); 
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
	public Object get(Object key){ 
		try {
			DatabaseEntry k = new DatabaseEntry(Utilities.toByteArray(key)); 
			DatabaseEntry v = new DatabaseEntry(); 
			if(database.get(null, k, v, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				byte[] retData = v.getData();
				return Utilities.toObject(retData);
			}else {
				return null;
			}
		}catch(Exception e2) {
			e2.printStackTrace();
		}
		return null;
	}
	 
	//未改 用了的时候再改成utilities.tobyteArray的形式
	public HashMap<String, Object> getAllKey() { 
		HashMap<String, Object> result = new HashMap<>();
		Cursor cursor = database.openCursor(null, null); 
		DatabaseEntry key = new DatabaseEntry(); 
		DatabaseEntry value = new DatabaseEntry(); 
		while(cursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) { 
			byte[] keyBytes = key.getData();
			if(keyBytes != null) { 
				byte[] valueBytes = value.getData();
				result.put((String)Utilities.toObject(keyBytes), Utilities.toObject(valueBytes)); 
				key = new DatabaseEntry(); 
			}else{
				break; 
			}
		} 
		cursor.close(); 
		return result; 
	}
	//按照键值删除数据
	public void del(Object key){
	    DatabaseEntry k = new DatabaseEntry(Utilities.toByteArray(key));  //键值转化
	    //Object value = get(key);                                //获取值
	    database.removeSequence(null, k);                       //删除值
	    //return value;                                           //返回删除的值
	}
}