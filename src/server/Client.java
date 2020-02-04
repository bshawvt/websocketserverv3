package server;

import java.util.Date;

import org.java_websocket.WebSocket;

import Dtos.AuthenticationDto;
import Models.UserAccountModel;
import server.blobs.MessageBlob;
import server.blobs.NetworkBlob;

public class Client {
	
	private int id = 0;
	private boolean ready = false; 
	private WebSocket connection = null;
	private boolean removed = false;
	private long startTime = 0;

	private String reason = ""; // reason for being kicked from server
	private boolean active = false;
	
	private AuthenticationDto authenticationDto;
	private UserAccountModel userAccount;
	
	private NetworkBlob frame;
	
	public Client() {
		this.startTime = (new Date()).getTime();
		this.frame = new NetworkBlob();
	}
	public Client(WebSocket connection, int id) {
		this.startTime = (new Date()).getTime();
		this.connection = connection; 
		this.ready = false;
		this.id = id;
		this.frame = new NetworkBlob();
	}
	
	public long getAuthStartTime() {
		return this.startTime;
	}
	public int getId() {
		return this.id;
	}
	public void setActive(boolean v) {
		this.active = v;
	}
	/**
	 * 
	 * @return  true when player is part of an active simulation node
	 */
	public boolean isActive() {
		return this.active;
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
	/**
	 * 
	 * @return true when client has authenticated and can send and receive network messages with the server  
	 */
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
		this.frame.add(message);
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
	public void setUserAccount(UserAccountModel userAccount) {
		this.userAccount = userAccount;	
	}
	public UserAccountModel getUserAccount() { return this.userAccount; }
	
}
