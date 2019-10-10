package server.blobs;

import com.google.gson.annotations.SerializedName;

public abstract class MessageBlob {
	public static class Type {
		public static final int None = 0;
		public static final int ChatBlob = 1;
	}
	
	@SuppressWarnings("rawtypes")
	final static public Class[] types = {
		DefaultBlob.class, // type: 0 // should never be 0
		ChatBlob.class,  // type: 1
	};
	
	@SerializedName("type")
	private int type;
	public int getType() { return this.type; }
	
}
