package threads;

import java.util.concurrent.LinkedBlockingQueue;

import server.ServerThreadMessage;

public class Threads {

	public static final int None = 0;
	public static final int Server = 1;
	public static final int Simulator = 2;
	public static final int Database = 3;
	public static final int Main = 4;
	
	private static final LinkedBlockingQueue<ServerThreadMessage> serverQueue = new LinkedBlockingQueue<>();
	//private static final LinkedBlockingQueue<SimulatorThreadMessage> SimulatorQueue;
	//private static final LinkedBlockingQueue<DatabaseThreadMessage> DatabaseQueue;
	
	public static LinkedBlockingQueue<ServerThreadMessage> getServerQueue() {
		return serverQueue;
	}
	
	public Threads() {
		
	}
	
}
