package json;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class NetworkBlob {
	
	@SerializedName("size")
	private int size;
	public int getSize() { return this.size; };

	@SerializedName("messages")
	private ArrayList<MessageBlob> messages;
	public ArrayList<MessageBlob> getMessages() { return this.messages; };
	
}
