package config;

public class Global {
	private static Global global = new Global();
	
	public static int maxBlockTxNum = 16;
	public static int minBlockTxNum = 4;
	public static Global getInstance() {
        return global;
    }
}
