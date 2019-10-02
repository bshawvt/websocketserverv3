package server;

import java.util.Date;

import org.java_websocket.WebSocket;

import Dtos.AuthenticationDto;

public class Client {
	
	private long id = 0;
	private boolean ready = false; // true if client has authenticated
	private WebSocket connection = null;
	private boolean removed = false;
	private long authStartTime = 0;
	private AuthenticationDto authenticationDto = null;
	private String reason = ""; // removed reason
	
	public Client() {
		this.authStartTime = (new Date()).getTime();
	}
	public Client(WebSocket connection, long id) {
		this.authStartTime = (new Date()).getTime();
		this.connection = connection; 
		this.ready = false;
		this.id = id;
	}
	public long getAuthStartTime() {
		return this.authStartTime;
	}
	public long getId() {
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
	public void sendMessage(String msg) {
		this.connection.send(msg);
	}
}
