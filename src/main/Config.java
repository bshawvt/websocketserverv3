package main;

public class Config {
	public static String ClientVersion = "1008190300"; // used during client authentication 
	public static int ServerPort;// game port clients connect through
	public static String DatabaseAddress; // sql server address
	public static String DatabaseUsername;
	public static String DatabasePassword;
	public static String DatabaseSchema;
	public static boolean DatabaseReporting = false; // server write to status table
	public static final boolean UseSSL = true;
	public static final int CharacterLimit = 3;
	public static final int NodeLimit = 1; // how many cpu cores simulator thread can use
	 
	
	public Config() {
		ServerPort = 29980;//UseSSL==true ? 443:29980;
		DatabaseSchema = "myschema";
		DatabaseAddress = "localhost:3306";//"jdbc:mysql://localhost:3306/" + DatabaseSchema + "?useSSL=false";
		DatabaseUsername = "dbuser";
		DatabasePassword = "dbpassword";
		
	}
	public Config(String[] args) {
		this();
		for(String arg : args) {
			String[] split = arg.split("=");
			String o = split[0];
			String v = split[1];

			if (o.equals("-port")) {
				ServerPort = Integer.parseInt(v);
			}
			else if (o.equals("-user")) {
				DatabaseUsername = v;
			}
			else if (o.equals("-password")) {
				DatabasePassword = v;
			}
			else if (o.equals("-schema")) {
				DatabaseSchema = v;
			}
			else if (o.equals("-address")) {
				DatabaseAddress = v;//"jdbc:mysql://" + v + "/" + DatabaseSchema + "?useSSL=false";
			}

		}
	}
	public static String getConnectionString() {
		return "jdbc:mysql://" + DatabaseAddress + "/" + DatabaseSchema + "?useSSL=false";
	}

}
