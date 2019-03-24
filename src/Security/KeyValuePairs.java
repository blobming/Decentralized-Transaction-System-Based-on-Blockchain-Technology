package Security;

import java.util.Base64;
import java.util.Map;

public class KeyValuePairs {

	private Map<String,Object> keyMap;
	private String publicKey;
	private String privateKey;
	
	public KeyValuePairs() {
		try {
			this.keyMap = SecretKey.initKey();
			this.setPublicKey(SecretKey.getPublicKeyStr(keyMap));
			this.setPrivateKey(SecretKey.getPrivateKeyStr(keyMap));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String Encrypt(String inputString,String publicKey) {
		try {
			byte[] cipherText = SecretKey.encrypt(inputString.getBytes(), publicKey);
			return Base64.getEncoder().encodeToString(cipherText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String Decrypt(String inputString) {
		try {
			byte[] plainText = SecretKey.decrypt(Base64.getDecoder().decode(inputString),privateKey);
			return new String(plainText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String Sign(String inputString) {
		try {
			byte[] signature=SecretKey.sign(inputString.getBytes(),privateKey);
			return Base64.getEncoder().encodeToString(signature);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public Boolean Verify(String inputString1,String inputString2,String publicKey) {
		try {
			return SecretKey.verify(inputString1.getBytes(), Base64.getDecoder().decode(inputString2),publicKey);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
