package Main;


import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import server.ServerThread;
import simulator.SimulatorThread;
import sql.SqlHelper;
import sql.SqlThread;
import thread.ThreadMessage;
import util.L;

public class WSSMainThread {
	public static void main(String[] args) {
		if (args.length == 4) new Config(args);
		else new Config();
		
		final LinkedBlockingQueue<ThreadMessage> ServerMessageQueue = new LinkedBlockingQueue<>();
		final LinkedBlockingQueue<ThreadMessage> SimulatorMessageQueue = new LinkedBlockingQueue<>();
		final LinkedBlockingQueue<ThreadMessage> DatabaseMessageQueue = new LinkedBlockingQueue<>();
		
		Thread server = new Thread(new ServerThread(ServerMessageQueue, SimulatorMessageQueue, DatabaseMessageQueue), "Server-0");
		server.setDaemon(false);
		server.start();
		
		Thread simulator = new Thread(new SimulatorThread(SimulatorMessageQueue, ServerMessageQueue, DatabaseMessageQueue), "Simulator-0");
		simulator.setDaemon(false);
		simulator.start();
		
		Thread database = new Thread(new SqlThread(SimulatorMessageQueue, ServerMessageQueue, DatabaseMessageQueue), "dbThread-0");
		database.setDaemon(false);
		database.start();
		
		// thread restart counters
		long simCount = 0;
		long sqlCount = 0; // todo: 
		long srvCount = 0;
		
		Scanner in = new Scanner(System.in);
		while(true) {
			
			if (in.hasNext()) {
				String line = in.nextLine();
				String[] lines = line.split("\\.");
				String _line = null;
				if(line.equals("exit") || line.equals("end") || line.equals("stop") || line.equals("quit")) { // keep forgetting how to shutdown from terminal so using all the words
					break;
				}
				else if (line.equals("help")) {
					// same reason as above
					System.out.println("exit - shutdown server\n"
							+ "simrestart - recreate the simulator thread\n"
							+ "srv|sim|db.<command> - here be dragons\n"
							+ "srv.broadcast <message>\n"
							+ "srv.dumpclient - get client data: [id] - [username] - [ping]\n"
							+ "sim.shutdown - stops the simulator thread\n");
				}
				else if (line.equals("simrestart")) {
					if (!simulator.isAlive()) {
						simCount++;
						simulator = null;
						simulator = new Thread(new SimulatorThread(SimulatorMessageQueue, ServerMessageQueue, DatabaseMessageQueue), "Simulator-" + simCount);
						simulator.setDaemon(false);
						simulator.start();
						ServerMessageQueue.add(new ThreadMessage(ThreadMessage.FROM_CONSOLE, ThreadMessage.TYPE_COMMAND, "thread_crash_simulator"));
						continue;
					}
					System.out.println("Main: aborted, thread is alive");
				}
				
				// thread specific actions
				else if (lines[0].equals("sim")) {
					_line = line.replace("sim.", "");
					SimulatorMessageQueue.add(new ThreadMessage(ThreadMessage.FROM_CONSOLE, ThreadMessage.TYPE_COMMAND, _line));
				}
				
				else if (lines[0].equals("srv")) {
					_line = line.replace("srv.", "");
					ServerMessageQueue.add(new ThreadMessage(ThreadMessage.FROM_CONSOLE, ThreadMessage.TYPE_COMMAND, _line));
				}
				else if (lines[0].equals("db")) {
					System.err.println("Main: todo: database command line not implemented");
					//_line = line.replace("db.", "");
					//_tMsgQueueSrv.add(new ThreadMessage(ThreadMessage.FROM_CONSOLE, ThreadMessage.TYPE_COMMAND, _line));
				}
			}
			
		}
		in.close();
		L.d("Main: Goodbye world!");
		System.exit(1);
	}

}
