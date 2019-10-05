package json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


public class MessageBlobDeserializer implements JsonDeserializer<MessageBlob> {
	// oddwarg stuffs
	@Override
	public MessageBlob deserialize(JsonElement json, Type arg1, JsonDeserializationContext ctx)
			throws JsonParseException {
		int type = json.getAsJsonObject().get("type").getAsInt(); 
		Class<? extends MessageBlob> blobType;
		if (type > 0 && type < MessageBlob.types.length) {
			blobType = MessageBlob.types[type];
			return ctx.deserialize(json, blobType);// new MessageBlobs(arg0.getAsJsonPrimitive().getAsString());
		}
		return null;//throw new IllegalArgumentException("MessageBlobDeserializer: unknown type " + type);
	}
}