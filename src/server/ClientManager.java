package server;

import java.util.ArrayList;
import java.util.Iterator;

import org.java_websocket.WebSocket;

import dtos.CharacterDto;
import dtos.UserAccountDto;
import server.BlobFactory.BlobType;
import server.Client.State;
import server.ServerThread.SocketServer;
import simulator.NetObjectSnapshot;
import sql.SqlWriter;
import thread.DatabaseMessage;
import thread.SimulatorMessage;
import thread.ThreadMessage;
import util.L;

public class ClientManager {
	
	private final long AUTH_MAX_ELAPSED = 2000;
	private final long PING_MAX_ELAPSED = 10000;
	
	private ServerThread server = null;
	private ArrayList<Client> clients = null;
	private ArrayList<Client> clientQueue = null;
	private ArrayList<QueueMessage> messageQueue = null;
	private long uuid = 1; // should never be 0?
	private SqlWriter sqlClient;
	//private long bandwidthOut = 0;
	

	public ClientManager (ServerThread serverThread) {
		this.server = serverThread;
		L.d("ClientManager: constructor");
		clients = new ArrayList<Client>();
		clientQueue = new ArrayList<Client>();
		messageQueue = new ArrayList<QueueMessage>();
		sqlClient = new SqlWriter();
		//bandwidthOut = 0;
	}
	public void sendNetObjectReset() {
		String updateMsg = ClientMessage.serializeMessageNetObjectReset();
		for(Client _client : clients) {//int i = 0; i < clients.size(); i++) {
			_client.pushMessage(updateMsg);
		}
		//flushConnections();
	}
	public void sendNetObjectRemove(SimulatorMessage simulatorMessage) {
		String updateMsg = ClientMessage.serializeMessageRemoveNetObject(simulatorMessage);
		for(Client _client : clients) {//int i = 0; i < clients.size(); i++) {
			_client.pushMessage(updateMsg);
		}
		//flushConnections();
	}
	public void sendNetObjectCreate(SimulatorMessage simulatorMessage) {
		String updateMsg = null;
		for(Client _client : clients) {//int i = 0; i < clients.size(); i++) {
			//long id = simulatorMessage.getClientId();
			//Client _client = clients.get(i);
			if (_client.getId() == simulatorMessage.getCharacterDto().getClientId()) {
				updateMsg = ClientMessage.serializeMessageCreateNetObject(simulatorMessage, 1);
			}
			else {
				updateMsg = ClientMessage.serializeMessageCreateNetObject(simulatorMessage);
			}
			_client.pushMessage(updateMsg);
		}
		//flushConnections();
	}
	public ServerThread getServerThread() {
		return server;
	}
	public Client getClient(int id) {
		return clients.get(id);
	}
	public Client getClientByUsername(String username) {
		for(Client _client : clients) {//int i = 0; i < clients.size(); i++) {
			//Client _client = clients.get(i);
			if (_client.getUnauthenticatedUsername().equals(username)) {
				return _client;
			}
		}
		return null;
	}
	public void addClient(WebSocket conn) {
		Client client = new Client(conn, uuid);
		clientQueue.add(client);
		uuid++;
		//System.out.println("added a client..");
	}
	public void removeClient(Client client) {
		if (client != null) {
			client.setRemoved(true);
			clientQueue.add(client);
			//getServerThread().dispatchSimulationThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, ThreadMessage.TYPE_REMOVE, client.getId()));
			if (client.getCharacterDto() != null)
			getServerThread().dispatchSimulationThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, ThreadMessage.TYPE_REMOVE, new SimulatorMessage(client.getCharacterDto()) ));
		}
	}
	public int getClientCount() {
		return clients.size();
	}
	synchronized public void flushQueue() {
		// flush connect/disconnected clients
		for(Iterator<Client> it = clientQueue.iterator() ; it.hasNext();) {
			Client item = it.next();
			//Client item = clientQueue.get(i);
			if (item != null)
				if (item.isRemoved()) {
					//item.send();
					_removeClient(item);
					L.d("ClientManager: flushQueue: removed a client from clients list");
				} else {
					clients.add(item);
					L.d("ClientManager: flushQueue: added new client to clients list");
				}
		}
		clientQueue.clear();
		
		for(Iterator<QueueMessage> mqItr = messageQueue.iterator(); mqItr.hasNext();) {
			QueueMessage message = mqItr.next();
			Client client = getClientByConnection(message.getConnection());
			if (client != null) {
				if(client.getState() != Client.State.READY) {
					if ((System.nanoTime() / 1000000) - client.getConnectionStartTime() > AUTH_MAX_ELAPSED ) {
						L.d("ClientManager: flushQueue: this client hasn't authenticated in time.. dropping");
						//client.getConnection().close();
						removeClient(client);
					}
					else {
						L.d("ClientManager: flushQueue: addBlobs: this client hasn't been authenticated yet.");
					}
				}

				if (client.isConnection(message.getConnection())) {
					L.d("ClientManager: flushQueue: this connection matches _client " + client.getId());
					new ClientMessage(this, client, message.getMessage());
					//return;
					mqItr.remove();
				}
			}
		}
		
		//messageQueue.clear();

		//for(Iterator<>)
	}
	public Client getClientByConnection(WebSocket connection) {
		for(Iterator<Client> it = clients.iterator() ; it.hasNext();) {
			Client client = it.next();
			if (client.getConnection() == connection) {
				L.d("ClientManager: findClientByConnection: found client " + client.getId());
				return client;
			}
		}
		return null;
	}
	public void sendDebug(String msg) {
		for(Client client : clients) {//int i = 0; i < clients.size(); i++) {
			//Client client = clients.get(i);
			String json = ClientMessage.serializeMessageDebug(msg);
			client.immediateSend(json);
		}
	}
	public void printAll(String msg) {
		server.broadcastMessage(msg);
	}
	private void _removeClient(Client client) {
		for(Iterator<Client> it = clients.iterator() ; it.hasNext();) {
			Client item = it.next();
			if (item.getId() == client.getId()) {
				long t = client.send();
				L.e("final send size: " + t);
				L.d("ClientManager: removeClient: found matching client " + item.getId() + ", dropping from ClientManager...");
				if (!item.getConnection().isClosed()) {
					item.getConnection().close();
				}
				L.d("ClientManager: removeClient: close");
				it.remove();
				L.d("ClientManager: removeClient: remove");
				break;
			}
		}
	}
	public void addBlobs(WebSocket conn, String msg) {
		/*
		 * checks if the connection sending the data is known, unknown, or in the process of identifying.
		 * if the user has not identified within a time span, then they are disconnected and removed from the system.
		 */
		//System.out.println(msg);
		//flushQueue();
		
		//messageQueue.add(new QueueMessage(conn, msg));
		
		for(Iterator<Client> it = clients.iterator() ; it.hasNext();) {
			Client _client = it.next();
			
			if(_client.getState() != Client.State.READY) {
				if ((System.nanoTime() / 1000000) -_client.getConnectionStartTime() > AUTH_MAX_ELAPSED ) {
					L.d("ClientManager: this client hasn't authenticated in time.. dropping");
					//_client.getConnection().close();
					removeClient(_client);
				}
				//else {
				//	L.d("ClientManager: addBlobs: this client hasn't been authenticated yet.xxx");
				//}
			}

			if (_client.isConnection(conn)) {
				//L.d("ClientManager: addBlobs: this connection matches _client " + _client.getId());
				//L.d("ClientManager: addBlobs: msg: " + msg);
				new ClientMessage(this, _client, msg);
				//return;
			}
			//L.d("ClientManager: addBlobs: id = " + _client.getId());
		}
		
		
	}

	synchronized public void updateClients() {
		// todo: better garbage
		for(Iterator<Client> it = clients.iterator() ; it.hasNext();) {
			Client c = it.next();
			if (c != null)
				if (!c.isRemoved()) {
					long time = (System.nanoTime() / 1000000);
					if (time > c.getLastPongTime() + 8000) {
						if (time > c.getPingTimeStart() + 5000) {
							c.immediateSend("{\"type\":" + BlobType.TYPE_PING + ",\"id\":" + c.getNextPingId() + ",\"timestamp\":" + time + ", \"prevLatency\":" + c.getLatency() + "}");
							c.setPingTimeStart(time);
						}
					}
					c.send();
				}
		}
	}
	public void sendFrame(NetObjectSnapshot snap) {
		if (snap != null)
			if (clients.size() > 0) {
				for(Iterator<Client> it = clients.iterator() ; it.hasNext();) {

					Client c = it.next();
					if (!c.isRemoved()) {
						if (snap.getClientId() == c.getId()) { // todo: need to tell the client if this netobject belongs to them
							c.pushMessage(ClientMessage.serializeMessageSyncNetObject(snap, 1));
						} else {
							c.pushMessage(ClientMessage.serializeMessageSyncNetObject(snap, 0));
						}
					}
				}
			}		
	}
	public void authenticateClientByDatabaseMessage(DatabaseMessage dbMessage) {
		if (dbMessage != null) {
			UserAccountDto accountDto = dbMessage.getUserAccountDto();
			if (accountDto !=null) {
				Client client = getClientByUsername(accountDto.getUsername());//getClientByClientId(dbMessage.getCharacterDto().getClientId());
				
				if (client != null) {
					accountDto.setLocked(true);
					client.setUserAccountDto(accountDto);
					
					client.setState(State.READY);
					//client.setCharacterDto(new CharacterDto(client));
					client.getCharacterDto().setOwner(accountDto.getId());
					
					
					getServerThread().dispatchSimulationThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, 
							ThreadMessage.TYPE_CREATE, 
							new SimulatorMessage(client.getCharacterDto()) ));
					client.pushMessage("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":1,\"message\": \"logged in as " + client.getUnauthenticatedUsername() + "\"}");
					printAll("Someone has connected...");
					return;
				}
				
			/*
				UserAccountDto tdto = dbMessage.getUserAccountDto();
				Client tClient = getClientByUsername(tdto.getUsername()); // check if the username is in use by any clients
				
				if (tdto.isLocked() == true || tClient != null) {
					
					tClient.immediateSend("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":0,\"message\": \"This account has been locked\"}");
					tClient.setRemoved(true);
					return;
					
				} else {
					
					Client client = getUnauthenticatedClient(tdto.getUsername());
					
					if (client!=null) {
					
						client.setUserAccountDto(tdto);
						client.setState(State.READY);
						client.setCharacterDto(new CharacterDto(client));
						
						getServerThread().dispatchSimulationThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, 
								ThreadMessage.TYPE_CREATE, 
								new SimulatorMessage(client.getCharacterDto()) ));
						client.pushMessage("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":1,\"message\": \"logged in as " + client.getUnauthenticatedUsername() + "\"}");
						printAll("Someone has connected...");
						return;
						
					}
				}*/
			
				/*if (tuser == null) {
					if (tdto.isLocked()) {
						tuser.immediateSend("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":0,\"message\": \"This account has been locked\"}");
						tuser.setRemoved(true);
						return;
					}
					
					tuser.setUserAccountDto(tdto);
					tuser.setState(State.READY);
					tuser.setCharacterDto(new CharacterDto(tuser));
					
					getServerThread().dispatchSimulationThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, 
							ThreadMessage.TYPE_CREATE, 
							new SimulatorMessage(tuser.getCharacterDto()) ));
					tuser.pushMessage("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":1,\"message\": \"logged in as " + tuser.getUsername() + "\"}");
					printAll("Someone has connected...");
					return;
				}*/
				//L.e("ClientMessage: deserializeBlob: auth: someone tried logging in as: " + tuser.getUsername() + " but had an bad details!");
				//tuser.immediateSend("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":0,\"message\": \"Failed to login. Try again!\"}");
				//tuser.getConnection().close();				
			}
		}
		/*UserAccountDto user = null;
					if ((user = clientManager.getSqlClient().getUserAccountOnVerify(messageLayer.getAuth().getUsername(), messageLayer.getAuth().getPassword())) != null) {//(messageLayer.getAuth().getUsername().length() > 3) { 
						Client _tClient = clientManager.getClientByUsername(messageLayer.getAuth().getUsername());
						if (user.isLocked() == true || _tClient != null) { // if this user account is already connected boot both connections out because I dont know how to handle this situation right now
							long _out = client.immediateSend("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":0,\"message\": \"This account has been locked\"}");
							//clientManager.addBandwidthOut(_out);
							//client.getConnection().close();
							client.setRemoved(true);
							return;
						}

						client.setUserAccountDto(user);
						client.setUsername(messageLayer.getAuth().getUsername());
						client.setState(State.READY);
						
						// on auth, create dto with client id and account owner id
						client.setCharacterDto(new CharacterDto(client));
						
						clientManager.getServerThread().dispatchSimulationThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, 
								ThreadMessage.TYPE_CREATE, 
								new SimulatorMessage(client.getCharacterDto()) ));
						client.pushMessage("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":1,\"message\": \"logged in as " + messageLayer.getAuth().getUsername() + "\"}");
						clientManager.printAll("Someone has connected...");
						continue;
					}
					else {
						L.e("ClientMessage: deserializeBlob: auth: someone tried logging in as: " + messageLayer.getAuth().getUsername() + " but had an bad details!");
						long _out = client.immediateSend("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":0,\"message\": \"Failed to login. Try again!\"}");
						//clientManager.addBandwidthOut(_out);
						client.getConnection().close();
						return;
					}*/
	}
	private Client getClientByClientId(long id) {
		for(Iterator<Client> it = clients.iterator() ; it.hasNext();) {
			Client c = it.next();
			if (c != null)
				if (c.getId() == id) {
					return c;
				}
		}
		return null;
	}
	private Client getUnauthenticatedClient(String username) {
		for(Client _client : clients) {
			if (_client.getUnauthenticatedUsername().equals(username)) {
				return _client;
			}
		}
		return null;
	}
}
