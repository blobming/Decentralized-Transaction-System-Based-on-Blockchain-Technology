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

	
	public static String Encrypt(String text,String publicKey) {
		try {
			byte[] cipherText = SecretKey.encrypt(text.getBytes(), publicKey);
			return Base64.getEncoder().encodeToString(cipherText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String Decrypt(String cipherText, String privateKey) {
		try {
			byte[] plainText = SecretKey.decrypt(Base64.getDecoder().decode(cipherText),privateKey);
			return new String(plainText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String Sign(String text, String privateKey) {
		try {
			byte[] signature=SecretKey.sign(text.getBytes(),privateKey);
			return Base64.getEncoder().encodeToString(signature);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Boolean Verify(String plainText,String signedText,String publicKey) {
		try {
			return SecretKey.verify(plainText.getBytes(), Base64.getDecoder().decode(signedText),publicKey);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
