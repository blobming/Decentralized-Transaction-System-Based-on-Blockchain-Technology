package Security;
 
 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.util.Base64;
 
 
public class SecretKey {
    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    public static final String SIGNATURE_ALGORITHM="MD5withRSA";
     /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    
  //map对象中存放公私钥
    public static Map<String, Object> initKey() throws Exception {
        //获得对象 KeyPairGenerator 参数 RSA 1024个字节
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        //通过对象 KeyPairGenerator 获取对象KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();
        
        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }
    
    //获得公钥字符串
    public static String getPublicKeyStr(Map<String, Object> keyMap) throws Exception {
        //获得map中的公钥对象 转为key对象
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }
 
 
    //获得私钥字符串
    public static String getPrivateKeyStr(Map<String, Object> keyMap) throws Exception {
        //获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }
    
    //获取公钥
    public static PublicKey getPublicKey(String key) throws Exception {  
        byte[] keyBytes;  
        keyBytes = Base64.getDecoder().decode(key);  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PublicKey publicKey = keyFactory.generatePublic(keySpec);  
        return publicKey;  
    }  
    
    //获取私钥
    public static PrivateKey getPrivateKey(String key) throws Exception {  
        byte[] keyBytes;  
        //keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);  
        return privateKey;  
    }
    
    //解码返回byte
    public static byte[] decryptBASE64(String key) throws Exception {
        //return (new BASE64Decoder()).decodeBuffer(key);
        return Base64.getDecoder().decode(key);
    }
 
 
    //编码返回字符串
    public static String encryptBASE64(byte[] key) throws Exception {
        //return (new BASE64Encoder()).encodeBuffer(key);
    	return Base64.getEncoder().encodeToString(key);
    }
    
    //***************************签名和验证*******************************  
    public static byte[] sign(byte[] data,String privateKeyStr) throws Exception{  
      PrivateKey priK = getPrivateKey(privateKeyStr);  
      Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);       
      sig.initSign(priK);  
      sig.update(data);  
      return sig.sign();  
    }  
    
    public static boolean verify(byte[] data,byte[] sign,String publicKeyStr) throws Exception{  
      PublicKey pubK = getPublicKey(publicKeyStr);  
      Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);  
      sig.initVerify(pubK);  
      sig.update(data);  
      return sig.verify(sign);  
    }  
    
  //************************加密解密**************************  
    public static byte[] encrypt(byte[] plainText,String publicKeyStr){  
        PublicKey publicKey;
		try {
			publicKey = getPublicKey(publicKeyStr);
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);  
	        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
	        int inputLen = plainText.length;
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        int offSet = 0;
	        int i = 0;
	        byte[] cache;
	        while (inputLen - offSet > 0) {
	            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
	                cache = cipher.doFinal(plainText, offSet, MAX_ENCRYPT_BLOCK);
	            } else {
	                cache = cipher.doFinal(plainText, offSet, inputLen - offSet);
	            }
	            out.write(cache, 0, cache.length);
	            i++;
	            offSet = i * MAX_ENCRYPT_BLOCK;
	        }
	        byte[] encryptText = out.toByteArray();
	        out.close();  
	        return encryptText;  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return null;
    }  
      
    public static byte[] decrypt(byte[] encryptText,String privateKeyStr){  
        
		try {
			PrivateKey privateKey = getPrivateKey(privateKeyStr);  
	        Cipher cipher;
			cipher = Cipher.getInstance(KEY_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);  
	        int inputLen = encryptText.length;
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        int offSet = 0;
	        byte[] cache;
	        int i = 0;
	        // 对数据分段解密
	        while (inputLen - offSet > 0) {
	            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
	                cache = cipher.doFinal(encryptText, offSet, MAX_DECRYPT_BLOCK);
	            } else {
	                cache = cipher.doFinal(encryptText, offSet, inputLen - offSet);
	            }
	            out.write(cache, 0, cache.length);
	            i++;
	            offSet = i * MAX_DECRYPT_BLOCK;
	        }
	        byte[] plainText = out.toByteArray();
	        out.close();  
	        return plainText; 
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
         return null;
    }  
 
    // only for testing
    public static void main(String[] args) {
        Map<String, Object> keyMap;
        byte[] cipherText;
        String input = "Hello World!";
        try {
            keyMap = initKey();
            String publicKey = getPublicKeyStr(keyMap);
            System.out.println("公钥------------------");
            System.out.println(publicKey);
            String privateKey = getPrivateKeyStr(keyMap);
            System.out.println("私钥------------------");
            System.out.println(privateKey);
            
            System.out.println("测试可行性-------------------");
            
            System.out.println("明文======="+input);
            
            cipherText = encrypt(input.getBytes(),publicKey); 
            //加密后的东西
            //System.out.println("密文======="+new String(cipherText));
            String tempStr = Base64.getEncoder().encodeToString(cipherText);
            System.out.println("密文======="+tempStr);
            //开始解密 
            //byte[] plainText = decrypt(cipherText,privateKey);
            byte[] plainText = decrypt(Base64.getDecoder().decode(tempStr),privateKey);
            System.out.println("解密后明文===== " + new String(plainText)+"\n");
            
            ////test pass!!
            System.out.println("验证签名-----------");
            String str="Testtest";
            System.out.println("原文:"+str);
            byte[] signature=sign(str.getBytes(),privateKey);
            String tempVal = Base64.getEncoder().encodeToString(signature);
            System.out.println(tempVal);
            boolean status=verify(str.getBytes(), Base64.getDecoder().decode(tempVal),publicKey);
            System.out.println("验证情况："+status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}