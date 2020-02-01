package server;

import org.java_websocket.WebSocket;

import Dtos.AuthenticationDto;
import threads.Threads;

public class ServerThreadMessage {
	
	public static class Type {
		public static final int None = 0;
		public static final int Update = 1;
		public static final int Authenticate = 2;
		public static final int Flush = 3;
	}
	
	private final int from;
	private final int type;
	private final String command;
	private final Object dto;

	
	public String getCommand() {
		return command;
	}
	public int getFrom() {
		return from;
	}
	public int getType() {
		return type;
	}
	public Object getDto() {
		return this.dto;
	}

	
	public ServerThreadMessage() {
		this.from = Threads.None;
		this.type = Type.None;
		this.command = null;
		this.dto = null;
	}
	public ServerThreadMessage(int from, int type) {
		this.from = from;
		this.type = type;
		
		this.command = null;		
		this.dto = null;
	}
	public ServerThreadMessage(int from, int type, String command) {
		this.from = from;
		this.type = type;
		this.command = command.toLowerCase();
		
		this.dto = null;
	}
	public ServerThreadMessage(int from, int type, Object dto) {
		this.from = from;
		this.type = type;
		this.dto = dto;
		
		this.command = null;
	}

}
