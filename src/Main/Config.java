package main;

public class Config {
	public static String ClientVersion = "0.0.07262019"; // used during client authentication 
	public static int ServerPort;// game port clients connect through
	public static String DatabaseAddress; // sql server address
	public static String DatabaseUsername;
	public static String DatabasePassword;
	public static boolean DatabaseReporting = false; // server write to status table
	public static boolean UseSSL = false;
	
	public Config() {
		ServerPort = 29980;
		DatabaseAddress = "jdbc:mysql://localhost:3306/mydatabase?useSSL=false";
		DatabaseUsername = "dbuser";
		DatabasePassword = "dbpassword";
		
	}
	public Config(String[] args) {
		ServerPort = Integer.parseInt(args[3]);
		DatabaseAddress = args[2];
		DatabaseUsername = args[0];
		DatabasePassword = args[1];
		
	}

}
