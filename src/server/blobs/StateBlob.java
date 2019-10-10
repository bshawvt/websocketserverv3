package server.blobs;

import com.google.gson.annotations.SerializedName;

public class StateBlob extends MessageBlob {
	@SerializedName("inputState")
	private long inputState;
	public long getInputState() { return this.inputState; };
}
