package config;

public class Global {
	private static Global global = new Global();
	
	public static final int maxBlockTxNum = 16;
	public static final int minBlockTxNum = 4;
	public static final int nbits = 4; //用于与其他节点同步获取合适的nbits
	public static final double subsidy = 0.02;
}
