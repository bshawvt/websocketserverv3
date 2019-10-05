package json;

import com.google.gson.annotations.SerializedName;

public abstract class MessageBlob {
	final static public Class[] types = {
		Object.class, // type: 0 // should never be 0
		ChatBlob.class,  // type: 1
	};
	
	@SerializedName("type")
	private int type;
	public int getType() { return this.type; }
	
}
