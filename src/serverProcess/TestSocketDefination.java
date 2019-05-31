package serverProcess;

import com.google.gson.Gson;

import Security.KeyValuePairs;
import obj.Status;
import obj.User;
import obj.UserTransmissionGson;

public class TestSocketDefination {

	public static void main(String args[]) {
		System.out.println("发送报文示例");
		UserTransmissionGson transUser = new UserTransmissionGson("LOGIN", "xjm.good@gmail.com", "xujiaming666");
		System.out.println(new Gson().toJson(transUser));
		transUser = new UserTransmissionGson("REGISTER", "xjm.good@gmail.com", "xujiaming666");
		System.out.println(new Gson().toJson(transUser));
		
		System.out.println("返回报文示例");
		KeyValuePairs pair = new KeyValuePairs();
		User theUser = new User(transUser.getName(),transUser.getPass(),pair.getPublicKey(), pair.getPrivateKey());
		System.out.println(new Gson().toJson(new Status("1", theUser))+"_FIN");
		System.out.println("注册错误报文示例");
		System.out.println(new Gson().toJson(new Status("0", "Invalid username"))+"_FIN");
		System.out.println(new Gson().toJson(new Status("0", "Invalid password"))+"_FIN");
		System.out.println(new Gson().toJson(new Status("0", "Username has been taken"))+"_FIN");
		System.out.println("登录错误报文示例");
		System.out.println(new Gson().toJson(new Status("0", "Login Failed"))+"_FIN");
	}
}
