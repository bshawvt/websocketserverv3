package simulator;

import json.ChatBlob;
import threads.Threads;

public class SimulatorThreadMessage {
	
	
	public static class Type {
		public static final int None = 0;
		public static final int Update = 1;
		public static final int Add = 2;
		public static final int Remove = 3;
	}
	private final int from;
	private final int type;
	private final String command;
	public int getFrom() { return this.from; }
	public int getType() { return this.type; }
	public String getCommand() { return this.command; }
	
	public SimulatorThreadMessage() {
		this.from = Threads.None;
		this.type = Type.None;
		this.command = null;
	}
	
	public SimulatorThreadMessage(int from, int type) {
		this.from = from;
		this.type = type;
		this.command = null;
	}
	
	public SimulatorThreadMessage(int from, int type, String command) {
		this.from = from;
		this.type = type;
		this.command = command;
	}
	
}
