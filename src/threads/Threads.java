package threads;

import java.util.concurrent.LinkedBlockingQueue;

import database.DatabaseThreadMessage;
import server.ServerThreadMessage;
import simulator.SimulatorThreadMessage;

public class Threads {

	public static final int None = 0;
	public static final int Server = 1;
	public static final int Simulator = 2;
	public static final int Database = 3;
	public static final int Main = 4;
	
	private static final LinkedBlockingQueue<ServerThreadMessage> serverQueue = new LinkedBlockingQueue<>();
	private static final LinkedBlockingQueue<SimulatorThreadMessage> SimulatorQueue = new LinkedBlockingQueue<>();
	private static final LinkedBlockingQueue<DatabaseThreadMessage> DatabaseQueue = new LinkedBlockingQueue<>();
	
	public Threads() {
		
	}
	
	public static LinkedBlockingQueue<ServerThreadMessage> getServerQueue() {
		return serverQueue;
	}
	
	public static LinkedBlockingQueue<SimulatorThreadMessage> getSimulatorQueue() {
		return SimulatorQueue;
	}

	public static LinkedBlockingQueue<DatabaseThreadMessage> getDatabaseQueue() {
		return DatabaseQueue;
	}
	
}
