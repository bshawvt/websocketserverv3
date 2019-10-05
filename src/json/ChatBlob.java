package json;

import com.google.gson.annotations.SerializedName;



public class ChatBlob extends MessageBlob {
	@SerializedName("message")
	private String message;
	public String getMessage() { return this.message; };
}