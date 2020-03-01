package server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.ssl.PEMTokenJava11;
import org.java_websocket.ssl.SSL;

import Dtos.AuthenticationDto;
import Dtos.CharacterDto;

import Dtos.StateChangeDto;
import Models.CharacterModel;
import database.DatabaseThreadMessage;
import main.Config;

import server.blobs.JoinBlob;
import server.blobs.CharacterBlob;
import server.blobs.ChatBlob;
import server.blobs.MessageBlob;
import server.blobs.NetworkBlob;
import server.blobs.StateBlob;
import server.chat.ChatManager;
import simulator.SimulatorThreadMessage;
import simulator.netobjects.NetObject;
import threads.Threads;
import ui.Form;

public class Server extends WebSocketServer {
	
	public static class Reason {
		public static final int None = 4100;
		public static final int Clean = 4101; 
		public static final int FailedAuthStep = 4102;
		public static final int TokenIsConsumed = 4103;
		public static final int AccountInUse = 4104;
	}
	
	public Clients clients;
	private ChatManager chatManager;
	//private ArrayList<Client> clients;
	//private ArrayList<Client> clientQueue;
	//private long clientCounter = 0; // becomes client ID
	
	public Server() {
		super(new InetSocketAddress(Config.ServerPort));
		this.setReuseAddr(true);
		this.setTcpNoDelay(true);
		
		this.chatManager = new ChatManager(this);
		this.clients = new Clients();
		
		
		//clients = new ArrayList<Client>();
		//clientQueue = new ArrayList<Client>();
		
		if (Config.UseSSL)
			try {
				System.out.println("Server: using ssl!");
				this.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(new SSL()
						.getSSLContextFromLetsEncrypt(Config.SSLCertPath, Config.SSLKeyPassword, new PEMTokenJava11())));
				
				System.out.println("Server: bound to port " + Config.ServerPort);
			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			finally {
				
			}
		
	}
	
	@Override
	public void onStart() {
		System.out.println("Server: has started");
		
	}
	
	@Override
	public void stop() {
		try {
			super.stop();
			// todo: shutdown 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onError(WebSocket connection, Exception exception) {
		
		System.out.println(connection + " has an error");
		Client client = connection.getAttachment();
		if (client != null) {
			if (client.isReady()) {
			}
			
			System.out.println(" !!! " + client.getUserAccount().getUsername() + 
					" experienced a connection error");
			client.setRemoved(true);
		}
		exception.printStackTrace();
	}

	@Override
	public void onClose(WebSocket connection, int code, String message, boolean remote) {
		System.out.println(connection + " has closed their connection, code: " + code + ", message: " + message);
		// remove connection from clients list
		Client client = connection.getAttachment();//clients.getClient(connection);
		if (client != null) { // clients are only prepared when they have valid auth tokens
			if (client.isReady()) { // do something special if the user was authenticated
				//Threads.getSimulatorQueue().offer();
			}
			if (client.isActive()) {
				CharacterDto dto = new CharacterDto(client);
				Threads.getSimulatorQueue().offer(new SimulatorThreadMessage(Threads.Server, SimulatorThreadMessage.Type.Remove, dto));
			}
			client.setRemoved(true);
			
			Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Server, ServerThreadMessage.Type.Flush));
		}
	}


	@Override
	public void onMessage(WebSocket connection, String message) {
		
		System.out.println(connection + " sent: " + message);
		Client client = connection.getAttachment();//clients.getClient(connection);
		System.out.println("connection attachment: " + connection.getAttachment());
		
		// filter out messages from users who aren't authenticated
		if (client != null && client.isReady()) {
			System.out.println("Server: todo: received message from authenticated user:");
			
			NetworkMessage netMessage = new NetworkMessage(client);
			NetworkBlob netBlob = netMessage.deserialize(message);
			
			// user sent junk that wasn't a NetworkMessage
			if (netBlob == null) {
				System.out.println("Server: netblob was JUNK!");
				return;
			}

			for(int i = 0; i < netBlob.getMessages().size(); i++) {

				MessageBlob messageBlob = netBlob.getMessages().get(i);
				
				// should never happen but in case user sends junk
				if (messageBlob == null) continue;
				
				propagateNetworkMessage(client, messageBlob);				
				
			}
			
			Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Server, ServerThreadMessage.Type.Flush));
			return;
		}
		
		// a connection is sending stuff without authenticating first
		if (client != null) {
			System.out.println("Server: todo: received message from unauthenticated user");
			client.setRemoved(true); /* the server should kick the player before this could ever happen, 
									 but just in case kick the client to prevent more messages we're not ready for*/
		}
		return;
	}
	
	private void propagateNetworkMessage(Client client, MessageBlob message) {
		int type = message.getType();
		if (type > 0 && type < MessageBlob.types.length) {
			//Class<? extends MessageBlob> blobType = MessageBlob.types[message.getType()];

			switch(type) {
			
				// client has sent a chat message
				case MessageBlob.Type.ChatBlob: {
					ChatBlob chatBlob = (ChatBlob) message;
					chatBlob.setFrom(client.getId()); // 
					chatManager.sort(chatBlob);
					break;
				}
				
				// client is selecting one of their characters to join simulation with
				case MessageBlob.Type.JoinBlob: {
					JoinBlob authBlob = (JoinBlob) message;
					
					// prevent pesky curious user problems
					if (client.isActive()) {
						System.err.println("User has tried to send a join blob while being part of an active simulation!");
						return;
					}
					
					// user is joining the game as a new character
					if (authBlob.id == -1) {
						if (client.getAuthenticationDto().getCharacters().size() < Config.CharacterLimit) {
							System.out.println("User has created a new character");
							
							insertClientIntoSim(client, null);
							
							/* the users number of characters is only set at login so 
							 * it must be increased after creating a new character otherwise 
							 * a user can create as many characters as they like */
							client.getAuthenticationDto().getCharacters().add(null);
						}
						else {
							System.err.println("User has tried to create a new character but they have the maximum already");
						}
					}
					
					// user has chosen one of their already existing characters
					else {
						int size = client.getAuthenticationDto().getCharacters().size();
						if (size > 0 && (authBlob.id >= 0 && authBlob.id < size) ) {
							CharacterModel character = client.getAuthenticationDto().getCharacters().get(authBlob.id);
							if (character != null) {
								System.out.println("User has chosen their character named " + character.getCharacterName());
								//CharacterDto dto = new CharacterDto(client, character);
								// insert the user into the simulator
								//Threads.getSimulatorQueue().offer(new SimulatorThreadMessage(Threads.Server, SimulatorThreadMessage.Type.Add, dto));
								
								//clients.promoteClientToPlayer(client);
								insertClientIntoSim(client, character);
							}
							else {
								System.err.println("User has chosen a null character some how!!");
							}
						}
						else {
							System.err.println("User has tried to select a character index that is out of bounds!!");
						}
					}
					break;
				}
				
				// should never happen
				default: { 
					System.out.println("junk message blob");
					break;
				}
			}
		}
	}
	
	public void update(StateChangeDto dto) {
		if (dto == null) return;
		Iterator<NetObject> it = dto.to.iterator();
		
		while (it.hasNext()) {
			NetObject netObject = it.next();
			Client client = clients.playerTable.get(netObject.clientId);
			if (client == null || client.isRemoved()) continue; // prevents broadcasting disconnected user their disconnect event			
			StateBlob blob = new StateBlob(dto);

			if (netObject.clientId == dto.who.clientId)
				blob.me = true;
			client.addFrame(blob);
			//}
		}
	}

	private void insertClientIntoSim(Client client, CharacterModel character) {
		CharacterDto dto;
		if (character != null)
			dto = new CharacterDto(client, character);
		else { 
			dto = new CharacterDto(client, client.getUserAccount().getUserId());
            Threads.getDatabaseQueue().offer(new DatabaseThreadMessage(Threads.Server, DatabaseThreadMessage.Type.AddCharacter, dto));
			// the owner id needs to be filled in for the simulator otherwise owner id will be 0
			//dto.getCharacterModel().setCharacterOwner(client.getAuthenticationDto().getUserAccount().getUserId());
		}
		
		// server will await a message from the simulator confirming the user is ready to receive game states
		Threads.getSimulatorQueue().offer(new SimulatorThreadMessage(Threads.Server, SimulatorThreadMessage.Type.Add, dto));
		
		
		//client.setActive(true);
		clients.promoteClientToPlayer(client);
		
		
	}

	@Override
	public void onOpen(WebSocket connection, ClientHandshake handshake) {

		System.out.println(connection.getRemoteSocketAddress().getHostString()/* + "/" + connection.getLocalSocketAddress().getHostString() */+ " has connected");
		String[] connectionString = handshake.getResourceDescriptor().split("/");
		
		// connection string must be exact or treat this connection as junk
		if (connectionString.length == 3) {
			String token = connectionString[1];
			String clientVersion = connectionString[2];
			
			// the token length is for a sha256 hash
			if (token.length() < 60) {
				System.out.println("Server: onOpen: received a bad token");
				connection.close(Reason.FailedAuthStep, "Bad authentication token");
				return;
			}
			
			// client version checking
			if (clientVersion.compareTo(Config.ClientVersion) == 0) {
				
				// preparing the client for server
				Client client = clients.addClient(connection);
				// the dto is shared among most of the threads and should be treated as immutable 
				AuthenticationDto dto = new AuthenticationDto();
				client.setAuthenticationDto(dto); // 
				
				// set now for database consumeSessionToken
				dto.setToken(token);
				dto.setOwnerAddress(connection.getRemoteSocketAddress().getHostString());

				// 
				dto.setClient(client);
				
				// send dto with token to database to consume token
				Threads.getDatabaseQueue().offer(new DatabaseThreadMessage(Threads.Server, DatabaseThreadMessage.Type.Authentication, dto));
							
				// todo: flush
				// todotodo: the server doesn't update clients until a flush event
				Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Server, ServerThreadMessage.Type.Flush));
				
				return;
			}
			
			// disconnect the user
			else {
				System.out.println("Server: onOpen: received a bad client version");
				connection.close(Reason.FailedAuthStep, "Bad client version");
				return;
			}
		}
		
		// the junk message!
		else {
			System.out.println("Server: onOpen: received a bad connection string");
			connection.close(Reason.FailedAuthStep, "Failed to authenticate");
			return;
		}
	}
	
	public void prepareClient(AuthenticationDto dto) {
		
		System.out.println("Server: authenticating connection:");
		
				
		Client client = dto.getClient();//clients.getClient(dto.getOwner());
		if (client == null) { // this should never happen
			System.out.println("... ! could not authenticate client because it was never tracked! client probably closed socket");
			return;
		}
		client.setUserAccount(dto.getUserAccount());
		
		// if useraccount is null then the database thread did not consume the token
		if (dto.getUserAccount() == null) {
			System.out.println("... client " + dto.getClient().getId() + " did not provide a valid token!");
			client.setRemoved(true, "Failed to authenticate");
			return;
		}
		
		// check if the account has been locked
		if (dto.getUserAccount().isLocked()) {
			System.out.println("... " + dto.getUserAccount().getUsername() + " tried to access a locked account");
			client.setRemoved(true, "Account is locked");
			return;
		}
		
		// check if the account is in use by any of the clients before proceeding
		if (clients.isUsernameInUse(dto.getUserAccount().getUsername(), dto.getClient().getId()) == true) {
			System.out.println("... " + dto.getUserAccount().getUsername() + " already in use!");
			client.setRemoved(true, "Account in use");
			return;
		}
		
		System.out.println("... " + dto.getUserAccount().getUsername() + " has authenticated!");
		Form.addClientToList(dto.getUserAccount().getUsername());
		// the client is ready to send and receive messages 
		client.setReady(true);
		
		// provide player with a selection of their characters
		JoinBlob joinBlob = new JoinBlob();
		joinBlob.ready = true;

		int c = 0;
		for(CharacterModel model : dto.getCharacters()) {
			joinBlob.characters.add(new CharacterBlob(model, c++));
		}
		// push blob to client
		client.addFrame(joinBlob);
	}
	
	public void flush() {
		clients.flush();
	}


}
