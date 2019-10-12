package server;

import threads.Threads;

public class ServerThread implements Runnable {
	
	private Server server;
	public ServerThread() {
		System.out.println("ServerThread: Hello world!");
		this.server = new Server();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//Server server;

		/*if (Config.UseSSL) {
			System.out.println("ServerThread has set Config.ServerPort to 443");
			Config.ServerPort = 443;
		}*/
		//server = new Server();
		server.start();
		
		// flush thread messages
		doThreadMessageFlushLoop();

	}
	
	public void doThreadMessageFlushLoop() {
		ServerThreadMessage msg = null;
		try {
			while((msg = Threads.getServerQueue().take()) != null) {				
				int type = msg.getType();
				int from = msg.getFrom();
				
				switch(type) {
					case ServerThreadMessage.Type.None: {
						System.out.println("ServerThread: ServerQueue: received no event");
						if (from == Threads.Main) {
							System.out.println("... from Main thread!");
							processCommand(msg.getCommand());
						}
						break;
					}
					case ServerThreadMessage.Type.Update: {
						System.out.println("ServerThread: ServerQueue: received update event");
						break;
					}
					case ServerThreadMessage.Type.Authenticate: {
						System.out.println("ServerThread: ServerQueue: received authenticate event");
						if (from == Threads.Database) {
							System.out.println("... from Database thread!");
							server.authenticateClient(msg.getAuthenticationDto());
						}
						break;
					}
					case ServerThreadMessage.Type.Flush: {
						System.out.println("ServerThread: ServerQueue: received flush event");
						break;
					}
					default: {
						break;
					}
				}
				// flush connections
				// todo: this is sooo bad 
				server.flush();				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processCommand(String command) {
		if (command.equals("help")) {
			System.out.println("flush <frames> - send network frames");
		}
		else if (command.equals("flush frames")) {
			System.out.println("doing server.flush() ... ");
			server.flush();
		}
		else {
			System.out.println("ServerThread: processCommand: unknown command:\n... " + command);
		}
	}
}
