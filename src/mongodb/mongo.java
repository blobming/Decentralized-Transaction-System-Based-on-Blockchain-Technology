package mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class mongo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{   
		       // 连接到 mongodb 服务
		         MongoClient mongoClient = new MongoClient( "localhost" , 27017);
		       
		         // 连接到数据库
		         MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");  
		       System.out.println("Connect to database successfully");
		       mongoDatabase.createCollection("bitcoin");
		       System.out.println("集合创建成功");
		        
		      }catch(Exception e){
		        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		     }
	}

}
