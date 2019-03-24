package Utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilities {
	/**
     * A hashing method that changes a string (like a URL) into a hash suitable for using as a
     * disk filename.
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
  //序列化对象
    public static byte[] toByteArray (Object obj) {      
        if(obj == null)
            return null;
        byte[] bytes = null;      
        ByteArrayOutputStream bos = new ByteArrayOutputStream();      
        try {        
            ObjectOutputStream oos = new ObjectOutputStream(bos);         
            oos.writeObject(obj);        
            oos.flush();         
            bytes = bos.toByteArray();      
            oos.close();         
            bos.close();        
        } catch (IOException ex) {        
            //ex.printStackTrace();   
        }      
        return bytes;    
    }   

    //反序列化  
    public static Object toObject (byte[] bytes){    
        if(bytes == null)
            return null;
        Object obj = null;      
        try {        
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);        
            ObjectInputStream ois = new ObjectInputStream(bis);        
            obj = ois.readObject();      
            ois.close();   
            bis.close();   
        } catch (IOException ex) {        
            //ex.printStackTrace();   
        } catch (ClassNotFoundException ex) {        
            //ex.printStackTrace();   
        }
        return obj;    
    }
}
