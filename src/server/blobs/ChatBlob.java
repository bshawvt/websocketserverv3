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
	
	// to client only
	@SerializedName("from")
	private int from;
	public int getFrom() { return this.from; };
	public void setFrom(int v) { this.from = v; };
	
	public ChatBlob() {
		
	}
	public ChatBlob(int channelId, String message) {
		this.message = message;
		this.channelId = channelId;
		this.type = MessageBlob.Type.ChatBlob;
	}
}