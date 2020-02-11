package main;

public class Config {
	public static String ClientVersion = "1302020"; // used during client authentication 
	public static int ServerPort;// game port clients connect through
	public static String DatabaseAddress; // sql server address
	public static String DatabaseUsername;
	public static String DatabasePassword;
	public static String DatabaseSchema;
	public static int SnapshotLimit;
	public static int SnapshotDelay;
	public static boolean DatabaseReporting = false; // server write to status table
	public static final boolean UseSSL = true;
	public static final int CharacterLimit = 3;
	public static int NodeLimit = 4; // TODO: how many cpu cores simulator thread can use
	public static int NodeSize = 1000; // TODO: 
	public static String SSLCertPath = "E:\\acme";
	//public static final String SSLCertPath = "./certs/";
	public static String SSLKeyPassword = "";
	 
	
	public Config() {
		ServerPort = 29980;//UseSSL==true ? 443:29980;
		DatabaseSchema = "myschema";
		DatabaseAddress = "localhost:3306";//"jdbc:mysql://localhost:3306/" + DatabaseSchema + "?useSSL=false";
		DatabaseUsername = "dbuser";
		DatabasePassword = "dbpassword";
		SnapshotLimit = 10;
		SnapshotDelay = 100;
		
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
			else if (o.equals("-nodes")) {
				NodeLimit = Integer.parseInt(v);//"jdbc:mysql://" + v + "/" + DatabaseSchema + "?useSSL=false";
			}
			else if (o.equals("-snapshots")) {
				SnapshotLimit = Integer.parseInt(v);//"jdbc:mysql://" + v + "/" + DatabaseSchema + "?useSSL=false";
			}
			else if (o.equals("-snapdelay")) {
				SnapshotDelay = Integer.parseInt(v);//"jdbc:mysql://" + v + "/" + DatabaseSchema + "?useSSL=false";
			}
			else {
				System.out.println(	"\n============================="
									+ "\nUnknown argument: " + o
									+ "\nUse the following instead:"
									+ "\n-port=29980\t\t- game server port"
									+ "\n-user=dbuser\t\t- database user"
									+ "\n-password=dbpassword\t- database password"
									+ "\n-schema=dbschema\t- database schema"
									+ "\n-address=dbaddress\t- database connection string <ip>:<port>"
									+ "\n-nodes=1\t\t- limit of node threads" 
									+ "\n-snapshots=10\t\t- number of snapshots a node will create for each object"
									+ "\n-snapdelay=100\t\t- time delay between each snapshot"
									+ "\n=============================");
				System.exit(-1);
			}

		}
				
		System.out.println(	"=============================" +
							"\nstart up config" +
							"\n=============================" +
							"\nClientVersion: " + ClientVersion + 
							"\nServerPort:" + ServerPort +
							"\nDatabaseAddress: " + DatabaseAddress +
							"\nDatabaseUsername: " + DatabaseUsername +
							"\nDatabasePassword: " + "********" +
							"\nDatabaseSchema: " + DatabaseSchema +
							"\nDatabaseReporting: " + DatabaseReporting +
							"\nUseSSL: " + UseSSL +
							"\nCharacterLimit: " + CharacterLimit +
							"\nNodeLimit: " + NodeLimit +
							"\nNodeSize: " + NodeSize +
							"\nSSLCertPath: " + SSLCertPath +
							"\nSnapshotLimit: " + SnapshotLimit +
							"\nSnapshotDelay: " + SnapshotDelay +
							"\nSSLKeyPassword: " + "********" +
							"\n=============================");

	}
	public static String getConnectionString() {
		return "jdbc:mysql://" + DatabaseAddress + "/" + DatabaseSchema + "?useSSL=true";
	}

}
