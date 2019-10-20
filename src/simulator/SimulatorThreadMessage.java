package simulator;

import Models.CharacterModel;
import threads.Threads;

public class SimulatorThreadMessage {
	
	
	public static class Type {
		public static final int None = 0;
		public static final int Update = 1;
		public static final int Add = 2;
		public static final int Remove = 3;
	}
	private int from;
	private int type;
	private String command;
	public int getFrom() { return this.from; }
	public int getType() { return this.type; }
	public String getCommand() { return this.command; }
	
	private int clientId;
	public int getClientId() { return this.clientId; }
	private CharacterModel character;
	public CharacterModel getCharacter() { return this.character; }
	
	
	public SimulatorThreadMessage() {
		this.from = Threads.None;
		this.type = Type.None;
		this.command = null;
		this.clientId = -1;
	}
	
	public SimulatorThreadMessage(int from, int type) {
		this.from = from;
		this.type = type;

		this.clientId = -1;
		this.command = null;
	}
	
	public SimulatorThreadMessage(int from, int type, String command) {
		this.from = from;
		this.type = type;
		this.command = command;
		
		this.clientId = -1;
	}
	public SimulatorThreadMessage(int from, int type, int clientId, CharacterModel character) {
		this(from, type);
		this.clientId = clientId;
		this.character = character;
	}
	
}
