package server;

import org.java_websocket.WebSocket;

public class ServerThreadMessage {
	
	public static class Type {
		public static final int None = 0;
		public static final int Set = 1;
		public static final int Remove = 2;
	}
	
	private final int from;
	private final int type;
	private final String command;
	
	public String getCommand() {
		return command;
	}
	public int getFrom() {
		return from;
	}
	public int getType() {
		return type;
	}
	
	
	public ServerThreadMessage() {
		from = 0;
		type = 0;
		command = null;
	}
	public ServerThreadMessage(int from, int type, String command) {
		this.from = from;
		this.type = type;
		this.command = command;
	}
	public ServerThreadMessage(int from, int type, WebSocket connection) {
		this.from = from;
		this.type = type;
		this.command = null;
		System.out.println("key: " + connection.getResourceDescriptor());
	}
}
