package config;

public class Global {
	private static Global global = new Global();
	
	public static int maxBlockTxNum = 16;
	public static int minBlockTxNum = 4;
	public int nbits; //用于与其他节点同步获取合适的nbits
	public static Global getInstance() {
        return global;
    }
}
