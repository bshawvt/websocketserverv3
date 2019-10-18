package server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import com.sun.corba.se.impl.orb.ParserTable.TestAcceptor1;

import Dtos.AuthenticationDto;
import Models.CharacterModel;
import database.DatabaseThreadMessage;
import main.Config;
import server.blobs.AuthBlob;
import server.blobs.CharacterBlob;
import server.blobs.ChatBlob;
import server.blobs.MessageBlob;
import server.blobs.NetworkBlob;
import server.chat.ChatManager;
import threads.Threads;

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
				this.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(new SSL().getSSLContextFromLetsEncrypt()));
			}
			finally {
				System.out.println("Server: bound to port " + Config.ServerPort);
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
		// TODO Auto-generated method stub
		System.out.println(connection + " has an error");
		System.out.println(exception);
	}

	@Override
	public void onClose(WebSocket connection, int code, String message, boolean remote) {
		System.out.println(connection + " has closed their connection, code: " + code + ", message: " + message);
		// remove connection from clients list
		Client client = clients.getClient(connection);
		if (client != null) { // clients are only prepared when they have valid auth tokens
			if (client.isReady()) { // do something special if the user was authenticated
				//Threads.getSimulatorQueue().offer();
			}
			client.setRemoved(true);
			Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Server, ServerThreadMessage.Type.Flush));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(WebSocket connection, String message) {
		System.out.println(connection + " sent: " + message);
		Client client = clients.getClient(connection);
		// filter out messages from users who aren't authenticated
		if (client != null && client.isReady()) {
			System.out.println("Server: todo: received message from authenticated user:");
			
			NetworkMessage netMessage = new NetworkMessage(client);
			NetworkBlob netBlob = netMessage.deserialize(message);
			
			if (netBlob == null) {
				System.out.println("Server: netblob was JUNK!");
				return;
			}
			
			AuthenticationDto authDto = client.getAuthenticationDto(); 

			for(int i = 0; i < netBlob.getMessages().size(); i++) {

				MessageBlob messageBlob = netBlob.getMessages().get(i);
				if (messageBlob == null) continue; // this should never happen but just in case
				
				int type = messageBlob.getType();
				if (type > 0 && type < MessageBlob.types.length) {
					Class<? extends MessageBlob> blobType = MessageBlob.types[messageBlob.getType()];

					switch(type) {
						case MessageBlob.Type.ChatBlob: {
							ChatBlob chatBlob = (ChatBlob) messageBlob;
							chatBlob.setFrom(client.getId()); // 
							chatManager.sort(chatBlob);
							break;
						}
						case MessageBlob.Type.AuthBlob: {
							AuthBlob authBlob = (AuthBlob) messageBlob;
							
							// user is joining the game as a new character
							if (authBlob.id == -1) {
								if (authDto.getCharacters().size() < Config.CharacterLimit) {
									System.out.println("User has created a new character");
									
								}
								else {
									System.out.println("User has tried to create a new character but they have the maximum already");
								}
							}
							
							// user has chosen one of their already existing characters
							else {
								int size = authDto.getCharacters().size();
								if (size > 0 && (authBlob.id >= 0 && authBlob.id < size) ) {
									CharacterModel character = authDto.getCharacters().get(authBlob.id);
									if (character != null) {
										System.out.println("User has chosen their character named " + character.getCharacterName());
										
									}
								}
								else {
									System.out.println("Junk auth blob");
								}
							}
							break;
						}
						default: { // should never happen
							System.out.println("junk message blob");
							break;
						}
					}
				}
				
				
			}
			Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Server, ServerThreadMessage.Type.Flush));
			return;
		}
		// all other messages are from unauthorized users and should probably be purged immediately
		if (client != null) {
			System.out.println("Server: todo: received message from unauthenticated user");
			client.setRemoved(true);
		}
		return;
	}

	@Override
	public void onOpen(WebSocket connection, ClientHandshake handshake) {

		System.out.println(connection.getRemoteSocketAddress().getHostString()/* + "/" + connection.getLocalSocketAddress().getHostString() */+ " has connected");
		String[] uri = handshake.getResourceDescriptor().split("/");
		
		if (uri.length == 3) {
			String token = uri[1];
			String clientVersion = uri[2];
			
			if (token.length() <= 10) {
				System.out.println("Server: onOpen: received a bad token");
				connection.close(Reason.FailedAuthStep, "Bad authentication token");
			}
			else if (clientVersion.compareTo(Config.ClientVersion) == 0) {
				// prepare server for connection
				Client client = clients.addClient(connection);
				// create dto with token and new clients id to identify later
				AuthenticationDto dto = new AuthenticationDto();
				dto.setToken(token);
				dto.setOwner(client.getId());
				dto.setOwnerAddress(connection.getRemoteSocketAddress().getHostString());
				
				// ask database to verify and consume token
				Threads.getDatabaseQueue().offer(new DatabaseThreadMessage(Threads.Server, DatabaseThreadMessage.Type.Authentication, dto));
							
				// todo: flush
				Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Server, ServerThreadMessage.Type.Flush));
				
			}
			else {
				System.out.println("Server: onOpen: received a bad client version");
				connection.close(Reason.FailedAuthStep, "Bad client version");
			}
		}
		else {
			System.out.println("Server: onOpen: received a bad connection");
			connection.close(Reason.FailedAuthStep, "Failed to authenticate");
		}
	}
	
	public void prepareClient(AuthenticationDto dto) {
		
		System.out.println("Server: authenticating connection:");
				
		Client client = clients.getClient(dto.getOwner());
		if (client == null) { // this should never happen
			System.out.println("... ! could not authenticate client because it was never tracked! client probably closed socket");
			return;
		}
		
		// if useraccount is null then the database thread did not consume the token
		if (dto.getUserAccount() == null) {
			if (client != null) {
				System.out.println("... client " + dto.getOwner() + " did not provide a valid token!");
				client.setRemoved(true, "Failed to authenticate");
			}
			return;
		}
		
		// check if the account has been locked
		if (dto.getUserAccount().isLocked()) {
			System.out.println("... " + dto.getUserAccount().getUsername() + " tried to access a locked account");
			client.setRemoved(true, "Account is locked");
			return;
		}
		
		// check if the account is in use by any of the clients before proceeding
		if (clients.getClient(dto.getUserAccount().getUsername()) != null) {
			System.out.println("... " + dto.getUserAccount().getUsername() + " already in use!");
			client.setRemoved(true, "Account in use");
			//client.getConnection().close(Reason.AccountInUse, "Account in use");
			return;
		}
		System.out.println("... " + dto.getUserAccount().getUsername() + " has authenticated!");
		client.setAuthenticationDto(dto);
		client.setReady(true);
		
		//
		clients.authenticateClient(client);
		
		// done, tell client to proceed to the next step
		//client.getConnection().send("Hello " + client.getAuthenticationDto().getUserAccount().getUsername());
		//clients.getPlayer(client.getId()).addFrame(new ChatBlob(0, "Hello " + client.getAuthenticationDto().getUserAccount().getUsername()));
		//client.addFrame(new ChatBlob(0, "Hello " + client.getAuthenticationDto().getUserAccount().getUsername()));
		AuthBlob auth = new AuthBlob();
		auth.ready = true;
		//dto.getCharacters().get(index);
		int c = 0;
		for(CharacterModel model : dto.getCharacters()) { 
			auth.characters.add(new CharacterBlob(model, c++));
		}
		client.addFrame(auth);
		//clients.flush();
		//client.sendMessage(new NetworkMessage().serialize(new NetworkBlob().s));	
		
	}
	
	public void flush() {
		clients.flush();
	}


}
