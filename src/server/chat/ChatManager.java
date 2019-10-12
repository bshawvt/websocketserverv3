package server.chat;

import java.util.HashMap;

import server.Client;
import server.Server;
import server.blobs.ChatBlob;

public class ChatManager {
	private HashMap<Integer, ChatChannel> channels;
	private Server server;
	
	public ChatManager(Server server) {
		this.server = server;
		this.channels = new HashMap<>();
		this.channels.put( 0, new ChatChannel("global") );
	}
	
	public void sort(ChatBlob blob) {
		int channelId = blob.getChannelId();
		ChatChannel channel = this.channels.get(channelId);
		if (channelId == 0) // global messages
			server.clients.playerTable.forEach((k, v) -> {
				v.addFrame(blob);
			});
		else if (channel != null) // private channel messages
			for (long id : channel.getActiveUsers()) {
				server.clients.getPlayer(id).addFrame(blob);
			}
	}
}
