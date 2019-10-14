package server.blobs;

import com.google.gson.annotations.SerializedName;

public abstract class MessageBlob {
	public static class Type {
		public static final int None = 0;
		public static final int ChatBlob = 1;
		public static final int AuthBlob = 2;
		public static final int StateBlob = 3;
	}
	
	@SuppressWarnings("rawtypes")
	final static public Class[] types = {
		DefaultBlob.class, // 0 // should never be 0
		ChatBlob.class,  // 1
		AuthBlob.class, // 2
		StateBlob.class // 3
	};
	
	@SerializedName("type")
	protected int type;
	public int getType() { return this.type; }
	
}
