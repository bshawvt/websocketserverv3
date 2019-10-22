package database;

import database.DatabaseThreadMessage;
import threads.Threads;

public class DatabaseThread implements Runnable {
	
	private Database db = null;
	public DatabaseThread() {
		System.out.println("DatabaseThread: Hello world!");
		this.db = new Database();
	}
	
	@Override
	public void run() {
		doThreadMessageFlushLoop();
	}
	
	public void doThreadMessageFlushLoop() {
		DatabaseThreadMessage msg = null;
		try {
			while ((msg = Threads.getDatabaseQueue().take()) != null) {
				int type = msg.getType();
				int from = msg.getFrom();
				
				switch (type) {
				
					// received a command from main thread or misc message
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
					
					// authentication messages get useraccounts
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
					
					// ???
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
