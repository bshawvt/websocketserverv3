package server;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;



public class NetworkMessage {
	/*
	 * 
	 */
	private Gson gson;
	private NetworkBlob blob;
	public NetworkMessage() {
		this.gson = new Gson();
	}
	public NetworkBlob deserialize(String data) {
		try {
			return gson.fromJson(data, NetworkBlob.class);
			
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
