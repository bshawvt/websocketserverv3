package server;

import java.util.Date;

import org.java_websocket.WebSocket;

import Dtos.AuthenticationDto;
import server.blobs.MessageBlob;
import server.blobs.NetworkBlob;

public class Client {
	
	private int id = 0;
	private boolean ready = false; // true if client has authenticated
	private WebSocket connection = null;
	private boolean removed = false;
	private long authStartTime = 0;
	//private AuthenticationDto authenticationDto = null;
	private String reason = ""; // removed reason
	
	private AuthenticationDto authenticationDto;
	
	private NetworkBlob frame;
	
	public Client() {
		this.authStartTime = (new Date()).getTime();
		this.frame = new NetworkBlob();
	}
	public Client(WebSocket connection, int id) {
		this.authStartTime = (new Date()).getTime();
		this.connection = connection; 
		this.ready = false;
		this.id = id;
		this.frame = new NetworkBlob();
	}
	
	public long getAuthStartTime() {
		return this.authStartTime;
	}
	public int getId() {
		return this.id;
	}
	public void setRemoved(boolean state) {
		this.removed = state;
	}
	public void setRemoved(boolean state, String reason) {
		this.removed = state;
		this.reason = reason;
	}
	public boolean isRemoved() {
		return this.removed;
	}
	public WebSocket getConnection() {
		return this.connection;
	}
	public void setReady(boolean state) { 
		this.ready = state; 
	}
	public boolean isReady() {
		return this.ready;
	}
	public AuthenticationDto getAuthenticationDto() {
		return this.authenticationDto;
	}
	public void setAuthenticationDto(AuthenticationDto dto) {
		this.authenticationDto = dto;
	}
	public void disconnect() {
		if (!this.connection.isClosed()) {  
			this.connection.close(Server.Reason.None, this.reason);
		}
	}
	public void addFrame(MessageBlob message) {
		this.frame.getMessages().add(message);
		//this.connection.send(message);
	}
	
	public void sendFrame() {
		if (this.frame.getMessages().size() > 0) {
			System.err.println("Client: sendFrame: send " + this.frame.getMessages().size());
			this.connection.send(new NetworkMessage().serialize(this.frame));
			clearFrame();
		}
	}
	
	public void clearFrame() {
		this.frame = new NetworkBlob();
	}
	
}
