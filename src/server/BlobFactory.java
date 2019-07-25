package server;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;


public class BlobFactory {
	/* probably shouldnt be called a factory but at this point idgaf */
	public BlobFactory() {
	
	}
	
	public class ProtoModel {
		@SerializedName("o")
		private ProtoLayerModel protoLayer = null;
		public ProtoLayerModel getProtoLayer() { return protoLayer; }
	}
	public class ProtoLayerModel {
		@SerializedName("size")
		private int size = 0;
		public int getSize() { return size; }
		@SerializedName("request")
		private long request = 0;
		public long getRequest() { return request; }
		@SerializedName("blobs")
		private ArrayList<MessageLayerModel> blobs = null;
		public ArrayList<MessageLayerModel> getBlobs() { return blobs; }
	}
	
	public class MessageLayerModel {
		@SerializedName("type")
		private int type = 0;
		public int getType() { return type; }
		@SerializedName("id")
		private long id = 0;
		public long getId() { return id; };
		@SerializedName("auth")
		private AuthBlobModel auth = null;
		public AuthBlobModel getAuth() { return auth; }
		@SerializedName("create")
		private CreateBlobModel create = null;
		public CreateBlobModel getCreate() { return create; };
		@SerializedName("chat")
		private ChatBlobModel chat = null;
		public ChatBlobModel getChat() { return chat; }
		@SerializedName("update")
		private UpdateBlobModel update = null;
		public UpdateBlobModel getUpdate() { return update; }
		@SerializedName("ping")
		private PingBlobModel ping = null;
		public PingBlobModel getPing() { return ping; }
		@SerializedName("command")
		private CommandBlobModel command = null;
		public CommandBlobModel getCommand() { return command; }
		
	}
	public class BlobType {
		public static final int TYPE_NONE 		= 0;
		public static final int TYPE_AUTH 		= 1;
		public static final int TYPE_CREATE 	= 2;
		public static final int TYPE_DISCONNECT = 3;
		public static final int TYPE_UPDATE 	= 4;
		public static final int TYPE_CHAT	 	= 5;
		public static final int TYPE_PING	 	= 6;
		public static final int TYPE_CONTENT 	= 7;
		public static final int TYPE_COMMAND	= 8;
	}
	public class AuthBlobModel {
		@SerializedName("username")
		private String username = null;
		public String getUsername() { return username; }
		@SerializedName("password")
		private String password = null;
		public String getPassword() { return password; }
		@SerializedName("version")
		private String version = null;
		public String getVersion() { return version; }
		
		// server outgoing only
		@SerializedName("id")
		private long id = 0;
		public long getId() { return id; }
		public void setId(long val) { id = val;	}
		@SerializedName("result")
		private long result = 0;
		public long getResult() { return result; }
		public void setResult(long val) { result = val; }
		@SerializedName("message")
		private String message = null;
		public String getMessage() { return message; }
		public void setMessage(String msg) { message = msg; }
	}
	public class CreateBlobModel {
		@SerializedName("username")
		private String username = null;
		public String getUsername() { return username; };
		public void setUsername(String name) { username = name; };
		@SerializedName("password")
		private String password = null;
		public String getPassword() { return password; };
		public void setPassword(String v) { password = v; };
		@SerializedName("email")
		private String email = null;
		public String getEmail() { return email; };
		public void setEmail(String v) { email = v; };
	}
	public class ChatType {
		public static final int TYPE_NONE = 0;
		public static final int TYPE_TEST = 1;
	}
	public class ChatBlobModel {
		@SerializedName("message")
		private String message = null;
		public String getMessage() { return message; }
	}
	public class UpdateType {
		public static final int TYPE_NONE = 0;
		public static final int TYPE_CREATE = 1;
		public static final int TYPE_REMOVE = 2;
		public static final int TYPE_SYNC = 3;
		public static final int TYPE_PLAYERSTATE = 4;
	}
	public class UpdateBlobModel {
		@SerializedName("updateType")
		private long updateType = 0;
		public long getUpdateType() { return updateType; }
		@SerializedName("state")
		private int state = 0;
		public int getState() { return state; }
		@SerializedName("angle")
		private double forwardAngle = 0.0;
		public double getForwardAngle() { return forwardAngle; };
		// server outgoing only
		@SerializedName("id")
		private long id = 0;
		public long getId() { return id; }
		@SerializedName("pitch")
		private double pitch = 0.0;
		public double getPitch() {
			return pitch;
		}
		
	}
	public class CommandBlobModel {
		@SerializedName("cmdType")
		private long type = 0;
		public long getCommandType() { return type; }
	}
	public class CommandType {
		public static final int TYPE_NONE = 0;
	}
	public class PingBlobModel  {
		@SerializedName("id")
		private long id = 0;
		public long getId() { return id; };
		public void setId(Long v) { id = v; };
		@SerializedName("timestamp")
		private long timestamp = 0;
		public long getTimestamp() { return timestamp; };
		public void setTimestamp(long v) { timestamp = v; };
		@SerializedName("type")
		private long type = 0;
		public long getType() { return type; };
		public void setType(long v) { type = v; };
	}
	
}