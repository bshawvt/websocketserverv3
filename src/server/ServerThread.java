package server;

import Dtos.AuthenticationDto;
import Dtos.StateChangeDto;
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
		server.setConnectionLostTimeout(5);
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
				
					// received input to process or a misc message
					case ServerThreadMessage.Type.None: {
						System.out.println("ServerThread: ServerQueue: received no event");
						if (from == Threads.Main) {
							System.out.println("... from Main thread!");
							processCommand(msg.getCommand());
						}
						break;
					}
					
					// state propagation between simulation thread and client
					case ServerThreadMessage.Type.Update: {
						System.out.println("ServerThread: ServerQueue: received update event");
						if (from == Threads.Simulator) {
							server.update((StateChangeDto) msg.getDto());
						}
						//server.update(msg.getDto());
						break;
					}
					
					// authentication messages are generated from onOpen 
					case ServerThreadMessage.Type.Authenticate: {
						System.out.println("ServerThread: ServerQueue: received authenticate event");
						if (from == Threads.Database) {
							System.out.println("... from Database thread!");
							
							// prepare client 
							server.prepareClient((AuthenticationDto) msg.getDto());
						}
						break;
					}
					
					// todo: 
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

	private void processCommand(String data) {
		String[] split = data.split(" ");
		String command = split[0];
		if (command.equals("help")) {
			System.out.println("you have been helped");
		}
		else if (command.equals("f")) {
			System.out.println("server flushed");
			server.flush();
		}
		else if (command.equals("kick")) {
			System.out.println("kicked some foo named " + split[1]);
			Client client = server.clients.getClientByUsername(split[1]);
			if (client != null) {
				if (split.length > 2) {
					String reason = data.split("``")[1];
					//System.out.println(reason);
					client.setRemoved(true, reason);
				}
				else {
					client.setRemoved(true);
				}
			}
		}
		else {
			System.out.println("ServerThread: processCommand: unknown command:\n... " + command);
		}
	}
}
