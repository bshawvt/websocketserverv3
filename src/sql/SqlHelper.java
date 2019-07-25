package sql;

import java.util.Random;

public class SqlHelper {
	
	public SqlHelper() {
		
	}
	
	public static byte[] Seed() {
		byte[] bytes = new byte[10];
		new Random().nextBytes(bytes);
		return bytes;
	}
	
}
