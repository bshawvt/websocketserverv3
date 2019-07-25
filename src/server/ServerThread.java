package server;

import java.awt.color.CMMException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.ServerRuntimeException;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import Main.Config;
import server.BlobFactory.BlobType;
import server.BlobFactory.ChatType;
import thread.DatabaseMessage;
import thread.ServerMessage;
import thread.SimulatorMessage;
import thread.ThreadMessage;
import util.L;
import util.Service;

public class ServerThread implements Runnable {
	
	private long serverTime = 0;
	private Service webReporting = null;
	private SocketServer server = null;
	private ClientManager cm = null;//new ClientManager(this);
	private final LinkedBlockingQueue<ThreadMessage> SimulatorMessageQueue;// me
	private final LinkedBlockingQueue<ThreadMessage> ServerMessageQueue;// = null;
	private final LinkedBlockingQueue<ThreadMessage> DatabaseMessageQueue;
	public ServerThread(LinkedBlockingQueue<ThreadMessage> _tMsgQueueSrv, LinkedBlockingQueue<ThreadMessage> _tMsgQueueSim, LinkedBlockingQueue<ThreadMessage> _tMsgQueueDb) {
		this.ServerMessageQueue = _tMsgQueueSrv;
		this.SimulatorMessageQueue = _tMsgQueueSim;
		this.DatabaseMessageQueue = _tMsgQueueDb;
		
		
	}
	public synchronized void flushThreadMessages() {
		ThreadMessage threadMessage = null;
	
		while((threadMessage = ServerMessageQueue.poll()) != null) {
			// todo:
			
			if (threadMessage.getFrom() == ThreadMessage.FROM_SIMULATOR) {
				//System.out.println("Server: received message from simulator");
				if (threadMessage.getType() == ThreadMessage.TYPE_CREATE) {
					getClientManager().sendNetObjectCreate(threadMessage.getSimMessage());	
				} else if (threadMessage.getType() == ThreadMessage.TYPE_REMOVE) {
					getClientManager().sendNetObjectRemove(threadMessage.getSimMessage());
				} else if (threadMessage.getType() == ThreadMessage.TYPE_GET) {
					
				} else if (threadMessage.getType() == ThreadMessage.TYPE_SET) {
					if (threadMessage.getSimMessage().getWorldFrame() != null) {
						if (threadMessage.getSimMessage().getWorldFrame().getSnapshots()!=null) {
							if (threadMessage.getSimMessage().getWorldFrame().getSnapshots().size() > 0)
								for(int i = 0; i < threadMessage.getSimMessage().getWorldFrame().getSnapshots().size(); i++) {
									getClientManager().sendFrame(threadMessage.getSimMessage().getWorldFrame().getSnapshots().get(i));
								}
						}
					}
					if (threadMessage.getSimMessage() != null) {// never step into?
						//getClientManager().sendNetObjectUpdate(_tMsg.getSimulatorMessage());
					}
					
				} else if (threadMessage.getType() == ThreadMessage.TYPE_NONE) {
					
				}
			}
			else if (threadMessage.getFrom() == ThreadMessage.FROM_DATABASE) {
				if (threadMessage.getType() == ThreadMessage.TYPE_GET) {
					System.out.println("Server: received message from database!");
						//_tMsg.getDbMessage().getCharacterDto();
					cm.authenticateClientByDatabaseMessage(threadMessage.getDbMessage());
					//}
					
				}
			}
			else if (threadMessage.getFrom() == ThreadMessage.FROM_CONSOLE) {
				if (threadMessage.getType() == ThreadMessage.TYPE_COMMAND) {
					processCommand(threadMessage.getData1());
				}
			}
			else if (threadMessage.getFrom() == ThreadMessage.FROM_SERVER) {
				if (threadMessage.getType() == ThreadMessage.TYPE_CREATE) {
					// client connected
					getClientManager().addClient(threadMessage.getSrvMessage().getConnection());
					
				} else if (threadMessage.getType() == ThreadMessage.TYPE_REMOVE) {
					// client disconnected
					getClientManager().removeClient(getClientManager().getClientByConnection(threadMessage.getSrvMessage().getConnection()));
					
				} else if (threadMessage.getType() == ThreadMessage.TYPE_SET) {
					// client message handling
					getClientManager().addBlobs(threadMessage.getSrvMessage().getConnection(), threadMessage.getSrvMessage().getMessage());
					
				}
			}
			
		}
		
	}	
	public void processCommand(String cmd) {
		String line = cmd;//.toLowerCase();
		String[] lines = line.split(" ");
		String _line = null;
		if (cmd.toLowerCase().equals("bandwidth")) {
			//System.out.println("outgoing: " + getClientManager().getBandwidthOut() + " bytes");
		}
		else if (cmd.toLowerCase().equals("thread_crash_simulator")) {
			System.err.println("Server: processCommand: thread_crash_simulator");
			for(int i = 0; i < getClientManager().getClientCount(); i++) {
				getClientManager().sendNetObjectReset();
				Client _client = getClientManager().getClient(i);
				dispatchSimulationThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, ThreadMessage.TYPE_CREATE, new SimulatorMessage(_client.getCharacterDto())));
			}
		}
		else if (cmd.toLowerCase().equals("connections")) {
			System.out.println("client connections: " + getClientManager().getClientCount() + " ");
		}
		else if (cmd.toLowerCase().equals("dumpclient")) {
			System.out.println("client connections: " + getClientManager().getClientCount() + " ");
			for(int i = 0; i < getClientManager().getClientCount(); i++) {
				Client _client = getClientManager().getClient(i);
				System.out.println("* " + _client.getId() + " - " + _client.getUnauthenticatedUsername() + " - " + _client.getAverageLatency());
			}
		}
		else if (lines[0].equals("test")) {
			ClientManager cm = getClientManager();
			_line = line.replace("test", "").trim();
			//System.out.println("line: " + (lines[1] != null ? lines[1] : "") + (lines[2] != null ? lines[2] : "") + (lines[3] != null ? lines[3] : ""));
			System.out.println("I am here");
			if (lines.length > 3) {
				System.out.println("and now, here");
				for(int i = 0; i < cm.getClientCount(); i++) {
					cm.getClient(i).immediateSend(ClientMessage.serializeMessageContent(0, (lines[1] != null ? lines[1] : ""), (lines[2] != null ? lines[2] : ""), (lines[3] != null ? lines[3] : "")));
				}
			}
		}
		else if (lines[0].equals("broadcast")) {
			ClientManager cm = getClientManager();
			cm.printAll(line.replace("broadcast ", ""));
		}
		else if (lines[0].equals("debug")) {
			System.out.println("debug message");
			ClientManager cm = getClientManager();
			L.d(line.replace("debug ", ""));
			cm.sendDebug(line.replace("debug ", ""));
		}
		else {
			L.d("> Unknown command");
		}
	}
	public synchronized ClientManager getClientManager() { return cm; };
	@Override
	public void run() {
		
		webReporting = new Service(600000, "webService");
		serverTime = System.nanoTime()/1000000;
		
		cm = new ClientManager(this);
		server = new SocketServer();
		server.startServer();
		while(true) {
			
			serverTime = System.nanoTime()/1000000;
			
			getClientManager().flushQueue(); // add/remove clients
			flushThreadMessages();
			getClientManager().updateClients();
			
			if (Config.DB_REPORTING)
				if (serverTime > webReporting.getLastUpdate() + webReporting.getInterval()) {
					webReporting.setLastUpdate(serverTime);
					//dispatchSqlThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, ThreadMessage.TYPE_SET, new DatabaseMessage(true)));
				}

		}
	}
	
	public void broadcastMessage(String message) {
		server.broadcast("{\"f\": { \"size\": 1, \"blobs\": [{ \"type\": " + BlobType.TYPE_CHAT + ", \"innerType\": " + ChatType.TYPE_NONE + ", \"message\": \"" + message + "\"}]}}");
	}
	public synchronized void dispatchSqlThreadMessage(ThreadMessage msg) {
		DatabaseMessageQueue.offer(msg);
	}
	public synchronized void dispatchSimulationThreadMessage(ThreadMessage msg) {
		SimulatorMessageQueue.offer(msg);
	}
	public synchronized void dispatchServerThreadMessage(ThreadMessage msg) {
		ServerMessageQueue.offer(msg);
	}
	
	
	public class SocketServer extends WebSocketServer {

		public static final int MAX_MESSAGE_LENGTH = 3000;		
		
		public SocketServer() {
			super(new InetSocketAddress(Config.SERVER_PORT));
			this.setReuseAddr(true);		
			this.setTcpNoDelay(true);

			L.d("Server: constructor: bound to port " + Config.SERVER_PORT);
			
		}
		public void startServer() {
			this.start();
			
		}
		public void stopServer() {
			try {
				L.d("Server: stopServer: stopped server");
				this.stop();
				
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void onMessage(WebSocket connection, String message) {
			if (message.length() > MAX_MESSAGE_LENGTH) {
				L.e("Server: onMessage: message length is greater than " + MAX_MESSAGE_LENGTH);
			} else {
				dispatchServerThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, ThreadMessage.TYPE_SET, new ServerMessage(connection, message)));//getClientManager().addBlobs(connection, message);
			}
		}

		@Override
		public void onOpen(WebSocket connection, ClientHandshake handshake) {
			L.d("Server: onOpen: " + handshake);
			//getClientManager().addClient(connection);
			dispatchServerThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, ThreadMessage.TYPE_CREATE, new ServerMessage(connection, "")));
		}
		
		@Override
		public void onClose(WebSocket connection, int code, String reason, boolean remote) {
			L.d("Server: onClose: " + code);
			//getClientManager().removeClient(getClientManager().getClientByConnection(connection));
			dispatchServerThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, ThreadMessage.TYPE_REMOVE, new ServerMessage(connection, "")));
		}

		@Override
		public void onError(WebSocket connection, Exception exception) {
			// TODO Auto-generated method stub
			L.e("Server: onError: conn: " + connection);
			L.e("Server: onError: E: " + exception);
			exception.printStackTrace();
		}
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			L.d("Server: onStart:");

		}
		
	}
	
}
