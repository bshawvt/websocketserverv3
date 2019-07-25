package Main;

public class Config {
	public static String CLIENT_VERSION = "0.0.11162018"; // used during client authentication 
	public static int SERVER_PORT;// game port clients connect through
	public static String DB_ADDRESS; // sql server address
	public static String DB_USER;
	public static String DB_PASS;
	public static boolean DB_REPORTING = false; // server write to status table
	
	public Config() {
		SERVER_PORT = 29980;
		DB_ADDRESS = "jdbc:mysql://localhost:3306/mydatabase?useSSL=false";
		DB_USER = "dbuser";
		DB_PASS = "dbpassword";
		
	}
	public Config(String[] args) {
		SERVER_PORT = Integer.parseInt(args[3]);
		DB_ADDRESS = args[2];
		DB_USER = args[0];
		DB_PASS = args[1];
		
	}

}
