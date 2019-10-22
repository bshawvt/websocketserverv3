package server.blobs;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class JoinBlob extends MessageBlob {
	
	@SerializedName("ready")
	public boolean ready;
	
	@SerializedName("characters")
	public ArrayList<CharacterBlob> characters;
	
	/**
	 * id sent by client to choose which of their characters from an authentication dto
	 */
	@SerializedName("id")
	public int id;
	
	public JoinBlob() {
		this.type = MessageBlob.Type.JoinBlob;
		this.characters = new ArrayList<>();
	}
	public JoinBlob(boolean ready) {
		this();
		this.ready = ready;
	}
	
	
}
