package mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class mongo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{   
		       // ���ӵ� mongodb ����
		         MongoClient mongoClient = new MongoClient( "localhost" , 27017);
		       
		         // ���ӵ����ݿ�
		         MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");  
		       System.out.println("Connect to database successfully");
		       mongoDatabase.createCollection("bitcoin");
		       System.out.println("���ϴ����ɹ�");
		        
		      }catch(Exception e){
		        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		     }
	}

}
