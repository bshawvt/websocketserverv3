package database;

import Dtos.AuthenticationDto;
import threads.Threads;

public class DatabaseThreadMessage {
	public static class Type {
		public static final int None = 0;
		public static final int Authentication = 1;
	}
	private final int from;
	private final int type;
	private final String command;
	private final String handshake;
	private final String sessionIP;
	private final AuthenticationDto authenticationDto;
	
	public DatabaseThreadMessage() {
		this.from = Threads.None;
		this.type = Type.None;
		this.command = null;
		this.handshake = null;
		this.sessionIP = null;
		this.authenticationDto = null;
	}
	public DatabaseThreadMessage(int from, int type, AuthenticationDto dto) {
		this.from = from;
		this.type = type;
		this.authenticationDto = dto;
		
		this.command = null;
		this.handshake = null;
		this.sessionIP = null;
	}
	public DatabaseThreadMessage(int from, int type, String command) {
		this.type = type;
		this.from = from;
		this.command = command;
		
		this.handshake = null;
		this.sessionIP = null;
		this.authenticationDto = null;
	}
	public int getFrom() {
		return this.from;
	}
	public int getType() {
		return this.type;
	}
	public String getHandshake() {
		return this.handshake;
	}
	public String getCommand() {
		return this.command;
	}
	public String getSessionIP() {
		return this.sessionIP;
	}
	public AuthenticationDto getAuthenticationDto() {
		return this.authenticationDto;
	}
}
