package berkeleyDb;
import java.io.File;

import com.sleepycat.je.*;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//同EnvironmentConfig来配置环境
	    EnvironmentConfig environmentConfig=new EnvironmentConfig();
	    environmentConfig.setTransactional(true);
	    environmentConfig.setAllowCreate(true);
	    //homeDirectory是数据库存放的目录
	    
	  //Open Environment
	    Environment environment=new Environment(new File("./"),environmentConfig);
	}

}
