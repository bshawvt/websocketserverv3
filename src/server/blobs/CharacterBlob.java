package server.blobs;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import Models.CharacterModel;

public class CharacterBlob {
	
	@SerializedName("id")
	public int id;
	
	@SerializedName("name")
	public String name;
	
	@SerializedName("description")
	public String description;
	
	public CharacterBlob(CharacterModel model, int id) {
		this.id = id;
		this.name = model.getCharacterName();
		this.description = model.getCharacterDescription();
	}
}
