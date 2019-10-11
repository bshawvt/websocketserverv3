package server.blobs;

import com.google.gson.annotations.SerializedName;



public class ChatBlob extends MessageBlob {
	
	/**/
	@SerializedName("channelId")
	private int channelId;
	public int getChannelId() { return this.channelId; }
	
	@SerializedName("message")
	private String message;
	public String getMessage() { return this.message; };
	
	
	public ChatBlob() {
		
	}
	public ChatBlob(int id, String message) {
		this.message = message;
		this.channelId = id;
		this.type = MessageBlob.Type.ChatBlob;
	}
}