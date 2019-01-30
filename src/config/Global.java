package config;

public class Global {
	private static Global global = new Global();
	
	public static int maxBlockTxNum = 8;
	public static Global getInstance() {
        return global;
    }
}
