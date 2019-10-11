package server;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import server.blobs.MessageBlob;
import server.blobs.MessageBlobDeserializer;
import server.blobs.NetworkBlob;



public class NetworkMessage {
	/*
	 * 
	 */
	private GsonBuilder builder = null;
	private Gson gson = null;
	public NetworkMessage() {
		this.builder = new GsonBuilder();
		this.gson = builder.create();
		

	}
	public NetworkMessage(Client client) {
		this.builder = new GsonBuilder();
		builder.registerTypeAdapter(MessageBlob.class, new MessageBlobDeserializer());
		this.gson = builder.create();
	}
	public NetworkBlob deserialize(String data) {
		try {
			//if (client == null) return null;
			//return gson.fromJson(data, NetworkBlob.class);
			
			return gson.fromJson(data, NetworkBlob.class);
			//return null;
			
		}
		catch (JsonParseException e) {
			System.out.println("NetworkMessage: deserialize jsonparse exception: " + e.getMessage());
		}
		return null;
	}
	
	public String serialize(NetworkBlob blob) {
		return gson.toJson(blob);
	}
	
	
	
	
}
