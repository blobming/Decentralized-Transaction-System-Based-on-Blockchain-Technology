package berkeleyDb;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.sleepycat.je.*;

import Utilities.Utilities;

public class MyBerkeleyDB {
	private static MyBerkeleyDB instance = null;
	private static Environment environment;
	private Database database;    
	private static String path;
	private String charset;
	private MyBerkeleyDB() {
		charset = "utf-8";
	}
	public static MyBerkeleyDB GetInstance() {
		if(instance == null) {
			instance = new MyBerkeleyDB();
			setEnvironment("./Data");	
		}
		return instance;
	}
	private static void setPath(String p) {
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
	private static void setEnvironment(String p){
		setPath(p); 
		//setChacheSize(chacheSize); 
		//配置环境
		EnvironmentConfig envConfig = new EnvironmentConfig(); 
		envConfig.setAllowCreate(true); 
		//envConfig.setCacheSize(this.chacheSize); 
		//创建环境
		environment = new Environment(new File(p),envConfig); 
	}
	//open database
	public void open(String dbName) { 
		DatabaseConfig dbConfig = new DatabaseConfig(); 
		dbConfig.setAllowCreate(true); 
		dbConfig.setSortedDuplicates(false); //不存储重复关键字
		database = environment.openDatabase(null, dbName, dbConfig); 
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
	
	public void put(byte[] key, byte[] value) {
		try {
			DatabaseEntry k = new DatabaseEntry(key); 
			DatabaseEntry v = new DatabaseEntry(value); 
			database.put(null, k, v); 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void put(String key, byte[] value) {
		try {
			DatabaseEntry k = new DatabaseEntry(key.getBytes(charset));
			DatabaseEntry v = new DatabaseEntry(value); 
			database.put(null, k, v); 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void put(byte[] key, String value) {
		try {
			DatabaseEntry k = new DatabaseEntry(key); 
			DatabaseEntry v = new DatabaseEntry(value.getBytes(charset)); 
			database.put(null, k, v); 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
<<<<<<< HEAD
	public Object get(String key){ 
		try {
			DatabaseEntry k = new DatabaseEntry(key.getBytes(charset)); 
			DatabaseEntry v = new DatabaseEntry(); 
			if(database.get(null, k, v, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				byte[] retData = v.getData();
				//String res = new String(retData,charset);
				return Utilities.toObject(retData);
			}else {
				return null;
			}
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch(Exception e2) {
			e2.printStackTrace();
=======
	public byte[] get(String key){ 
		DatabaseEntry k = new DatabaseEntry(key.getBytes(charset)); 
		DatabaseEntry v = new DatabaseEntry(); 
		if(database.get(null, k, v, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			byte[] retData = v.getData();
			//String res = new String(retData,charset);
			//return Utilities.toObject(retData);
			return retData;
		}else {
			return null;
>>>>>>> 607e82e710cc54fbee4d791e1b816c9673149672
		}
		return null;
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
	//按照键值删除数据
	public void del(Object key){
	    DatabaseEntry k = new DatabaseEntry(Utilities.toByteArray(key));  //键值转化
	    //Object value = get(key);                                //获取值
	    database.removeSequence(null, k);                       //删除值
	    //return value;                                           //返回删除的值
	}
}