package utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
            ex.printStackTrace();   
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
    
    /**
	 * 获得IP
	 * 
	 * @return 外网IP
	 */
	public static Map<String,String> getInternetIp() {
		try {
			Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
			Map<String,String> networkCardMap = new HashMap<String,String>();
			InetAddress ip = null;
			Enumeration<InetAddress> addrs;
			NetworkInterface networkInterface = null;
			while (networks.hasMoreElements()) {
				networkInterface = networks.nextElement();
				if(networkInterface.isUp()&& !networkInterface.isVirtual()&&!networkInterface.isLoopback()) {
					addrs = networkInterface.getInetAddresses();
					while (addrs.hasMoreElements()) {
						ip = addrs.nextElement();
						if (ip != null && ip instanceof Inet4Address && ip.isSiteLocalAddress()) {
							networkCardMap.put(networkInterface.getName(), ip.getHostAddress());
						}
					}
				}
			}
			return networkCardMap;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean checkUsername(String username) {
		return true;
	}
	
	public static boolean checkPwd(String pwd) {
		return true;
	}
}
