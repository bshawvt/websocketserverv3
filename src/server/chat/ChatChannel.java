package server.chat;

import java.util.ArrayList;

public class ChatChannel {
	private ArrayList<Long> playerTableId;
	private String name;
	
	public ChatChannel() {
		this.playerTableId = new ArrayList<>();
		this.name = "";
	}
	public ChatChannel(String name) {
		this.playerTableId = new ArrayList<>();
		this.name = name;
	}
	
	public void funnel() {
		playerTableId = new ArrayList<>();
		for (long id : playerTableId) {
			
		}
	}
	
	public ArrayList<Long> players() {
		return this.playerTableId;
	}
}
