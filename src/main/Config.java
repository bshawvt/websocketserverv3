package main;

public class Config {
	public static String ClientVersion = "923191400"; // used during client authentication 
	public static int ServerPort;// game port clients connect through
	public static String DatabaseAddress; // sql server address
	public static String DatabaseUsername;
	public static String DatabasePassword;
	public static String DatabaseSchema;
	public static boolean DatabaseReporting = false; // server write to status table
	public static boolean UseSSL = true;
	
	public Config() {
		ServerPort = UseSSL==true ? 443:29980;
		DatabaseSchema = "myschema";
		DatabaseAddress = "jdbc:mysql://localhost:3306/" + DatabaseSchema + "?useSSL=false";
		DatabaseUsername = "dbuser";
		DatabasePassword = "dbpassword";
		
	}
	public Config(String[] args) {
		ServerPort = Integer.parseInt(args[3]);
		DatabaseSchema = args[4];
		DatabaseAddress = "jdbc:mysql://" + args[2] + "/" + DatabaseSchema + "?useSSL=false";
		DatabaseUsername = args[0];
		DatabasePassword = args[1];
		
	}

}
