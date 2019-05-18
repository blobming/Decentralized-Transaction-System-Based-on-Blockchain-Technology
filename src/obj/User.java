package obj;

public class User {
	private String username;
	private String pwd;
	private String pubkey;
	private String privateKey;
	
	public User(String username, String pwd, String pubkey, String privateKey) {
		this.username = username;
		this.pwd = pwd;
		this.pubkey = pubkey;
		this.privateKey = privateKey;
	}
	public String getUsername() {
		return username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPubkey() {
		return pubkey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
}
