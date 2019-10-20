package server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.java_websocket.WebSocket;

import server.blobs.NetworkBlob;

public class Clients {
	
	public ArrayList<Client> clientList;
	public ArrayList<Client> clientQueue;
	
	public HashMap<Integer, Client> playerTable; // only added to when a client successfully authenticates with the server
	private int clientCounter; // becomes uuid for each client
	
	public Clients() {
		this.clientList = new ArrayList<>();
		this.clientQueue = new ArrayList<>();
		this.playerTable = new HashMap<Integer, Client>();
		this.clientCounter = 1; // 0 is saved for server
		
		
	}
	/**
	 * remove client connections and cleanup
	 * also sends client frames
	*/
	public void flush() {
			
		long now = (new Date()).getTime();
		
		// remove clients from list
		Iterator<Client> it = clientList.iterator();
		while(it.hasNext()) {
			Client client = it.next();
			if (client.isRemoved()) {
				System.out.println("Client: flush: removed client with id " + client.getId());
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
				System.out.println("Client: flush: removed client: client did not authenticate in time");
				System.out.println(playerTable.get(client.getId()));
				playerTable.remove(client.getId()); //
				System.out.println(playerTable.get(client.getId()));
				client.disconnect();
				it.remove();
				System.out.println("... clients list size: " + clientList.size());
			}
			else {
				// send network messages
				client.sendFrame();
			}
		}
		
		
		// merge new connections into client list
		Iterator<Client> qit = clientQueue.iterator();
		while(qit.hasNext()) {
			Client client = qit.next();
			System.out.println("Client: added new client with id " + client.getId());
			clientList.add(client);
			System.out.println("... clients list size: " + clientList.size());
		}
		clientQueue.clear();
		
		System.out.println("Client: clientList size: " + clientList.size());
		System.out.println("Client: playerTable size: " + playerTable.size());
		
	}
	/*
	public Client getClient(WebSocket connection) {
		System.out.println("Client: getClient: getting client from connection: ");
		
		// server never created a client for this connection
		if (connection.<Integer>getAttachment() == null) return null;
		
		Iterator<Client> it = clientList.iterator();
		while(it.hasNext()) {
			Client client = it.next();
			System.out.println("... current: " + client.getId());
			if (client.getId() == connection.<Integer>getAttachment()) {
				System.out.println("... found!");
				return client;
			}
			/*if (connection.getResourceDescriptor().equals("/" + client.getSessionToken())) {
				return client;
			}/
		}
		return null;
	}*/
	/*public Client getClient(int id) {
		System.out.println("Client: getClient: getting client from id: ");
		
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
	}*/
	public boolean isUsernameInUse(String username, int caller) {
		System.out.println("Client: isUsernameInUse: getting client from username: ");
		Iterator<Client> it = clientList.iterator();
		while(it.hasNext()) {
			Client client = it.next();
			
			if (client.getAuthenticationDto().getUserAccount().getUsername().equals(username) && client.getId() != caller ) {
				System.out.println("... found username with id matching caller: " + caller);
				return true;
			}
		}
		
		/*Iterator<Client> it = clientList.iterator();
		while(it.hasNext()) {
			Client client = it.next();
			if (client.getAuthenticationDto() == null) continue; // some times the dto is not set, meaning the server has never received an authentication from the database
			String currentName = client.getAuthenticationDto().getUserAccount().getUsername(); 
			System.out.println("... current: " + currentName);
			if (currentName.equals(username)) {
				System.out.println("... found!");
				return client;
			}
		}*/
		System.out.println("... client by username " + username + " not found");
		return false;
	}
	public Client getPlayer(int id) {
		return playerTable.get(id);
	}
	public Client addClient(WebSocket connection) {
		clientCounter++;
		Client client = new Client(connection, clientCounter);
		connection.setAttachment(client);
		clientQueue.add(client);
		return client;
	}
	
	public void promoteClientToPlayer(Client client) {
		//playerTable.put(client.getId(), client);
		System.out.println("Clients: " + client.getAuthenticationDto().getUserAccount().getUsername() + " has been added to the players table");
		playerTable.put(client.getId(), client);
	}
	
	
}
