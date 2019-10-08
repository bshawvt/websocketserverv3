package server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.java_websocket.WebSocket;

public class Clients {
	
	public ArrayList<Client> clientList;
	public ArrayList<Client> clientQueue;
	
	private HashMap<Long, Client> playerTable; // only added to when a client successfully authenticates with the server
	private long clientCounter; // acts as a uuid for each client
	
	public Clients() {
		this.clientList = new ArrayList<>();
		this.clientQueue = new ArrayList<>();
		this.playerTable = new HashMap<Long, Client>();
		this.clientCounter = 0;
		
		
	}
	
	public void getClient(int id) {
		
	}
	
	public void flush() {
			
		long now = (new Date()).getTime();
		
		// remove clients from list
		Iterator<Client> it = clientList.iterator();
		while(it.hasNext()) {
			Client client = it.next();
			if (client.isRemoved()) {
				System.out.println("Server: flush: removed client with id " + client.getId());
				if (client.isReady()) {
					System.out.println("... " + client.getAuthenticationDto().getUserAccount().getUsername() + " has disconnected!");
				}
				System.out.println("before: " + playerTable.get(client.getId()));
				playerTable.remove(client.getId()); //
				System.out.println("after: " + playerTable.get(client.getId())); 
				client.disconnect();
				it.remove();
				System.out.println("... clients list size: " + clientList.size());
			}
			// clean up stray connections
			else if ((now > client.getAuthStartTime() +  5000) && !client.isReady()) {
				System.out.println("Server: flush: removed client: client did not authenticate in time");
				System.out.println(playerTable.get(client.getId()));
				playerTable.remove(client.getId()); //
				System.out.println(playerTable.get(client.getId()));
				client.disconnect();
				it.remove();
				System.out.println("... clients list size: " + clientList.size());
			}
		}
		
		
		// merge new connections into client list
		Iterator<Client> qit = clientQueue.iterator();
		while(qit.hasNext()) {
			Client client = qit.next();
			System.out.println("Server: added new client with id " + client.getId());
			clientList.add(client);
			System.out.println("... clients list size: " + clientList.size());
		}
		clientQueue.clear();
		
		System.out.println("Server: clientList size: " + clientList.size());
		System.out.println("Server: playerTable size: " + playerTable.size());
		
	}
	
	public Client getClient(WebSocket connection) {
		System.out.println("Server: getClient: getting client from connection: ");
		
		// server never created a client for this connection
		if (connection.<Integer>getAttachment() == null) return null;
		
		Iterator<Client> it = clientList.iterator();
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
		
		Iterator<Client> it = clientList.iterator();
		while(it.hasNext()) {
			Client client = it.next();
			System.out.println("... current: " + client.getId());
			if (client.getId() == id) {
				System.out.println("... found!");
				return client;
			}
		}
		return null;
	}
	public Client getClient(String username) {
		System.out.println("Server: getClient: getting client from username: ");
		
		Iterator<Client> it = clientList.iterator();
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
		System.out.println("... client by username " + username + " not found");
		return null;
	}
	public Client getPlayer(long id) {
		return playerTable.get(id);
	}
	public Client addClient(WebSocket connection) {
		clientCounter++;
		connection.setAttachment(clientCounter);
		Client client = new Client(connection, clientCounter);
		clientQueue.add(client);
		return client;
	}
	
	public void authenticateClient(Client client) {
		//playerTable.put(client.getId(), client);
		System.out.println("Clients: " + client.getAuthenticationDto().getUserAccount().getUsername() + " has been added to the players table");
		playerTable.put(client.getId(), client);
	}
	
	
}
