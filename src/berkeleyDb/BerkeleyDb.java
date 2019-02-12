package berkeleyDb;

import java.io.File;

import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class BerkeleyDb {

	private static BerkeleyDb instance;
	private Environment env;
	
	
	private BerkeleyDb() {
	}
	
	public static BerkeleyDb GetInstance() {
		if(instance != null) return instance;
		else {
			BerkeleyDb.instance = new BerkeleyDb();
			return instance;
		}
	}
	
	public Environment GetEnvironment() {
		if(this.env != null) return this.env;
		else {
		    this.env=new Environment(new File("./Data"),BerkeleyDb.GetEnvironmentConfig());
		    return this.env;
		}
	}
	
	public static DatabaseConfig GetDbConfig() {
		DatabaseConfig dbconfig  = new DatabaseConfig();
	    dbconfig.setAllowCreate(true);
	    dbconfig.setTransactional(false);
	    dbconfig.setSortedDuplicates(false);
	    return dbconfig;
	}
	public static EnvironmentConfig GetEnvironmentConfig() {
		EnvironmentConfig environmentConfig=new EnvironmentConfig();
	    environmentConfig.setTransactional(true);
	    environmentConfig.setAllowCreate(true);
	    return environmentConfig;
	}
}
