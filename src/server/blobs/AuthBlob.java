package server.blobs;

import com.google.gson.annotations.SerializedName;

public class AuthBlob extends MessageBlob {
	@SerializedName("ready")
	public boolean ready;
	
	public AuthBlob() {
		this.type = MessageBlob.Type.AuthBlob;
	}
	public AuthBlob(boolean ready) {
		this();
		this.ready = ready;
	}
}
