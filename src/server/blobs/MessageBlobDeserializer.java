package server.blobs;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


public class MessageBlobDeserializer implements JsonDeserializer<MessageBlob> {
	// oddwarg stuffs
	@SuppressWarnings("unchecked")
	@Override
	public MessageBlob deserialize(JsonElement json, Type arg1, JsonDeserializationContext ctx)
			throws JsonParseException {
		
		Class<? extends MessageBlob> blobType = MessageBlob.types[0]; // fallback in case of junk data
		
		if (json.getAsJsonObject().has("type")) {
			//return null;//throw new IllegalArgumentException("MessageBlobDeserializer: blob contains no type");
			int type = json.getAsJsonObject().get("type").getAsInt();
			if (type > 0 && type < MessageBlob.types.length) {
				blobType = MessageBlob.types[type];
			}
		}
		return ctx.deserialize(json, blobType);// new MessageBlobs(arg0.getAsJsonPrimitive().getAsString());
		//return null;//throw new IllegalArgumentException("MessageBlobDeserializer: unknown type " + type);
	}
}