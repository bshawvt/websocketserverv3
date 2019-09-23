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
		DatabaseThreadMessage msg = null;
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
		}
	}

}
