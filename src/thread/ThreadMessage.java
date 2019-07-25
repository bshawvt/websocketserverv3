package thread;

public class ThreadMessage {
	
	// todo: wtf is this
	
	public static final int FROM_NONE 		= 0;
	public static final int FROM_SERVER 	= 1;
	public static final int FROM_SIMULATOR 	= 2;
	public static final int FROM_DATABASE 	= 3;
	public static final int FROM_CONSOLE	= 4;
	
	public static final int TYPE_NONE 		= 0;
	public static final int TYPE_CREATE 	= 1; // add client net object
	public static final int TYPE_REMOVE 	= 2; // remove client net object
	public static final int TYPE_GET 		= 3; // get data
	public static final int TYPE_SET 		= 4; // write data
	public static final int TYPE_COMMAND	= 5; // console command?
	
	private final int from;
	private final int type;
	private final String data1;
	
	private final SimulatorMessage simMessage;
	private final ServerMessage srvMessage;
	private final DatabaseMessage dbMessage;
	
	public ThreadMessage() {
		this.from = 0;
		this.type = 0;
		
		this.data1 = null;
		this.simMessage = null;
		this.srvMessage = null;
		this.dbMessage = null;
	}
	
	public ThreadMessage (int from, int type, String msg) { 
		this.from = from;
		this.type = type;
		this.data1 = msg;
		
		this.simMessage = null;
		this.srvMessage = null;
		this.dbMessage = null;
	}
	
	public ThreadMessage(int from, int type, SimulatorMessage msg) {
		this.from = from;
		this.type = type;		
		this.simMessage = msg;
		
		this.data1 = null;
		this.srvMessage = null;
		this.dbMessage = null;
	}
	
	public ThreadMessage(int from, int type, ServerMessage msg) {
		this.from = from;
		this.type = type;
		this.srvMessage = msg;
		
		this.simMessage = null;
		this.data1 = null;
		this.dbMessage = null;
	}
	
	public ThreadMessage(int from, int type, DatabaseMessage msg) {
		this.from = from;
		this.type = type;		
		this.dbMessage = msg;
		
		this.simMessage = null;
		this.data1 = null;
		this.srvMessage = null;
	}

	public int getFrom() {
		return from;
	}

	public int getType() {
		return type;
	}

	public String getData1() {
		return data1;
	}

	public SimulatorMessage getSimMessage() {
		return simMessage;
	}

	public ServerMessage getSrvMessage() {
		return srvMessage;
	}

	public DatabaseMessage getDbMessage() {
		return dbMessage;
	}
	
	
}