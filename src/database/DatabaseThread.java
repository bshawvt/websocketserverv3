package database;

import database.DatabaseThreadMessage;
import simulator.SimulatorThreadMessage;
import threads.Threads;

public class DatabaseThread implements Runnable {
	
	private Database db = null;
	public DatabaseThread() {
		System.out.println("DatabaseThread: Hello world!");
		this.db = new Database();
	}
	
	@Override
	public void run() {
		doThreadMessageFlush();
		/*DatabaseThreadMessage msg = null;
		try {
			while((msg = Threads.getDatabaseQueue().take()) != null) {
				int from = msg.getFrom();
				if (from == Threads.Server) {
					if (msg.getType() == DatabaseThreadMessage.Type.Authentication) {
						System.out.println("DatabaseThread: received auth from server");
						db.consumeSessionToken(msg.getAuthenticationDto());
					}
				}
				else if (from == Threads.Simulator) {
					
				}
				else if (from == Threads.Main) {
					if (msg.getCommand().equals("help")) {
						System.out.println("DatabaseThread: you have been helped!");
					}
					else {
						System.out.println("DatabaseThread: unknown command");
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public void doThreadMessageFlush() {
		DatabaseThreadMessage msg = null;
		try {
			while ((msg = Threads.getDatabaseQueue().take()) != null) {
				int type = msg.getType();
				int from = msg.getFrom();
				
				switch (type) {
					case DatabaseThreadMessage.Type.None: {
						if (from == Threads.Main) {
							String command = msg.getCommand();
							if (command != null) {
								System.out.println("DatabaseThread: received command from server");
								System.out.println("command: " + command);
							}
						}
						break;
					}
					case DatabaseThreadMessage.Type.Authentication: {
						System.out.println("DatabaseThread: received an Update event");
						if (from == Threads.Server) {
							System.out.println("... from Server!");
							System.out.println("DatabaseThread: received auth from server");
							db.consumeSessionToken(msg.getAuthenticationDto());
							break;
						}
						break;
					}
					default: {
						break;
					}
				
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
