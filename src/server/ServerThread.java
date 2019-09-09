package server;

import Dtos.AuthenticationDto;
import database.DatabaseThreadMessage;
import main.Config;
import threads.Threads;

public class ServerThread implements Runnable {
	
	public ServerThread() {
		System.out.println("ServerThread: Hello world!");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Server server;

		if (Config.UseSSL) {
			System.out.println("ServerThread has set Config.ServerPort to 443");
			Config.ServerPort = 443;
		}
		server = new Server();
		server.start();
		
		// flush thread messages
		ServerThreadMessage msg = null;
		try {
			while((msg = Threads.getServerQueue().take()) != null) {
				server.flush(); // flush server connections before updating Server state
				
				int type = msg.getType();
				int from = msg.getFrom();
				
				switch(type) {
					case ServerThreadMessage.Type.None: {
						System.out.println("ServerThread: ServerQueue: received no event");
						if (from == Threads.Main) {
							System.out.println("... from Main thread!");
							//server.command(msg.getCommand());
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
						server.flush();
						break;
					}
					default: {
						break;
					}
				}
				
			}
				/*
				int from = msg.getFrom();
				if (from == Threads.Server) {
					if (msg.getType() == ServerThreadMessage.Type.Add) {
						//server.addClient(msg.getClient());
					}
					else if (msg.getType() == ServerThreadMessage.Type.Remove) {
						//server.removeClient(msg.getClient());
					}					
				}
				
				
				
				
				else if (from == Threads.Database) {
					if (msg.getType() == DatabaseThreadMessage.Type.Auth) {
						System.out.println("ServerThread: received database authentication message");
						msg.getClient();
						System.out.println("asd");
						AuthenticationDto dto = msg.getAuthenticationDto();
						//server.authenticateConnection();
					}
				}
				
				
				
				
				else if (from == Threads.Main) {
					if (msg.getCommand().equals("help")) {
						System.out.println("ServerThread: you have been helped!");
					}
					else {
						System.out.println("ServerThread: unknown command");
					}
				}
			}*/
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
