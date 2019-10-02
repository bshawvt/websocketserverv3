package server;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class NetworkBlob {
	@SerializedName("size")
	private int size;
	public int getSize() { return this.size; };

	@SerializedName("messages")
	private ArrayList<MessageBlob> messages;
	public ArrayList<MessageBlob> getMessages() { return this.messages; };
	
	public class MessageBlob {
		@SerializedName("type")
		private int type;
		public int getType() { return this.type; };
		@SerializedName("message")
		private String message;
		public String getMessage() { return this.message; };		
	}
}
