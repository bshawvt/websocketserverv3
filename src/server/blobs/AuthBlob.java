package server.blobs;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class AuthBlob extends MessageBlob {
	
	@SerializedName("ready")
	public boolean ready;
	
	@SerializedName("characters")
	public ArrayList<CharacterBlob> characters;
	
	/**
	 * id sent by client to choose which of their characters from an authentication dto
	 */
	@SerializedName("id")
	public int id;
	
	public AuthBlob() {
		this.type = MessageBlob.Type.AuthBlob;
		this.characters = new ArrayList<>();
	}
	public AuthBlob(boolean ready) {
		this();
		this.ready = ready;
	}
	
	
}
