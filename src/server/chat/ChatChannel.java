package server.chat;

import java.util.ArrayList;

public class ChatChannel {
	private ArrayList<Long> activeUserIds;
	private String name;
	
	public ChatChannel() {
		this.activeUserIds = new ArrayList<>();
		this.name = "";
	}
	public ChatChannel(String name) {
		this.activeUserIds = new ArrayList<>();
		this.name = name;
	}
	
	public ArrayList<Long> getActiveUsers() {
		return this.activeUserIds;
	}
}
