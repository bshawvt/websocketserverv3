package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import Dtos.AuthenticationDto;
import database.DatabaseThreadMessage;
import main.Config;
import threads.Threads;

public class Server extends WebSocketServer {
	
	public static class Reason {
		public static final int None = 4100;
		public static final int Clean = 4101; 
		public static final int FailedAuthStep = 4102;
		public static final int TokenIsConsumed = 4103;
		public static final int AccountInUse = 4104;
	}
	
	private ArrayList<Client> clients;
	private ArrayList<Client> clientQueue;
	private long currentIndex = 0; // becomes client ID
	
	public Server() {
		super(new InetSocketAddress(Config.ServerPort));
		this.setReuseAddr(true);
		this.setTcpNoDelay(true);
		
		clients = new ArrayList<Client>();
		clientQueue = new ArrayList<Client>();
		
		if (Config.UseSSL)
			try {
				System.out.println("Server: using ssl!");
				this.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(new SSL().getSSLContextFromLetsEncrypt()));
			}
			finally {
				
			}
		System.out.println("Server: bound to port " + Config.ServerPort);
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
		Client client = getClient(connection);
		if (client != null) { // clients are only prepared when they have valid auth tokens
			if (client.isReady()) { // do something special if the user was authenticated
				//Threads.getSimulatorQueue().offer();
			}
			client.setRemoved(true);
			Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Server, ServerThreadMessage.Type.Flush));
		}
	}

	@Override
	public void onMessage(WebSocket connection, String message) {
		System.out.println(connection + " sent: " + message);
		Client client = getClient(connection);
		// filter out messages from users who aren't authenticated
		if (client != null && client.isReady()) {
			System.out.println("Server: todo: received message from authenticated user!");
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
		System.out.println(connection.getRemoteSocketAddress().getHostString() + " has connected");
		
		if (handshake.getResourceDescriptor().length() > 10) {
			System.out.println("Server: todo: token is set");

			// prepare server for connection
			Client client = addClient(connection);
			// create dto with token and new clients id
			AuthenticationDto dto = new AuthenticationDto();
			dto.setToken(handshake.getResourceDescriptor());
			dto.setOwner(client.getId());
			dto.setOwnerAddress(connection.getRemoteSocketAddress().getHostString());
			
			// ask database to verify this token
			Threads.getDatabaseQueue().offer(new DatabaseThreadMessage(Threads.Server, DatabaseThreadMessage.Type.Authentication, dto));
			
			
			//Threads.getDatabaseQueue().offer(new DatabaseThreadMessage(Threads.Server, DatabaseThreadMessage.Type.Auth, connection.getResourceDescriptor(), connection.getRemoteSocketAddress().getHostString()));
			// prepare the server for the new connection
			//Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Server, ServerThreadMessage.Type.Add, connection));
			
			Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Server, ServerThreadMessage.Type.Flush));
			
		}
		else {
			// todo: guest mode should be handled some day
			System.out.println("Server: received a connection without a token!");
			connection.close(Reason.FailedAuthStep, "Invalid auth token");
		}
	}
	
	public Client addClient(WebSocket connection) {
		currentIndex++;
		connection.setAttachment(currentIndex);
		Client client = new Client(connection, currentIndex);
		clientQueue.add(client);
		return client;
	}
	public void flush() {
		
		long now = (new Date()).getTime();
		
		// remove clients from list
		Iterator<Client> it = clients.iterator();
		while(it.hasNext()) {
			Client client = it.next();
			if (client.isRemoved()) {
				System.out.println("Server: flush: removed client with id " + client.getId());
				if (client.isReady()) {
					System.out.println("... " + client.getAuthenticationDto().getUserAccount().getUsername() + " has disconnected!");
				}
				if (!client.getConnection().isClosed()) {
					client.getConnection().close(Reason.None, "You have logged out");
				}
				it.remove();
				System.out.println("... clients list size: " + clients.size());
			}
			// clean up stray connections
			else if ((now > client.getAuthStartTime() +  5000) && !client.isReady()) {
				System.out.println("Server: flush: removed client: client did not authenticate in time");
				if (!client.getConnection().isClosed()) {
					client.getConnection().close(Reason.FailedAuthStep, "You did not authenticate");
				}
				it.remove();
				System.out.println("... clients list size: " + clients.size());
			}
		}
		
		// merge new connections into client list
		Iterator<Client> qit = clientQueue.iterator();
		while(qit.hasNext()) {
			Client client = qit.next();
			System.out.println("Server: added new client with id " + client.getId());
			clients.add(client);
			System.out.println("... clients list size: " + clients.size());
		}
		clientQueue.clear();
	}
	
	public Client getClient(WebSocket connection) {
		System.out.println("Server: getClient: getting client from connection: ");
		
		// server never created a client for this connection
		if (connection.<Integer>getAttachment() == null) return null;
		
		Iterator<Client> it = clients.iterator();
		while(it.hasNext()) {
			Client client = it.next();
			System.out.println("... current: " + client.getId());
			if (client.getId() == connection.<Long>getAttachment()) {
				System.out.println("... found!");
				return client;
			}
			/*if (connection.getResourceDescriptor().equals("/" + client.getSessionToken())) {
				return client;
			}*/
		}
		return null;
	}
	public Client getClient(long id) {
		System.out.println("Server: getClient: getting client from id: ");
		
		
		Iterator<Client> it = clients.iterator();
		while(it.hasNext()) {
			Client client = it.next();
			System.out.println("... current: " + client.getId());
			if (client.getId() == id) {
				System.out.println("... found!");
				return client;
			}
			/*if (connection.getResourceDescriptor().equals("/" + client.getSessionToken())) {
				return client;
			}*/
		}
		return null;
	}
	public Client getClient(String username) {
		System.out.println("Server: getClient: getting client from username: ");
		
		Iterator<Client> it = clients.iterator();
		while(it.hasNext()) {
			Client client = it.next();
			if (client.getAuthenticationDto() == null) continue; // some times the dto is not set, meaning the server has never received an authentication from the database
			String currentName = client.getAuthenticationDto().getUserAccount().getUsername(); 
			System.out.println("... current: " + currentName);
			if (currentName.equals(username)) {
				System.out.println("... found!");
				return client;
			}
		}
		return null;
	}
	
	public void authenticateClient(AuthenticationDto dto) {
		
		System.out.println("Server: authenticating connection:");
				
		Client client = getClient(dto.getOwner());
		if (client == null) { // this should never happen
			System.out.println("... MAJOR ERROR! could not authenticate client because it was never tracked!");
			return;
		}
		
		// if useraccount is null then the database thread did not consume the token
		if (dto.getUserAccount() == null) {
			if (client != null) {
				System.out.println("... client " + dto.getOwner() + " did not provide a valid token!");
				client.setRemoved(true);
			}
			return;
		}
		
		// check if the account is in use by any of the clients before proceeding
		if (getClient(dto.getUserAccount().getUsername()) != null) {
			System.out.println("... " + dto.getUserAccount().getUsername() + " already in use!");
			client.setRemoved(true);
			client.getConnection().close(Reason.AccountInUse, "This account is already in use!");
			return;
		}
		System.out.println("... " + dto.getUserAccount().getUsername() + " has authenticated!");
		client.setAuthenticationDto(dto);
		client.setReady(true);
		// done, tell client to proceed to the next step
		client.getConnection().send("Hello world!");
	}
}
