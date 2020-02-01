package Dtos;

import Models.CharacterModel;
import server.Client;

public class CharacterDto {
	private CharacterModel model;
	private Client client; // user this character dto was created for
	
	public CharacterDto() {
		
	}
	public CharacterDto(Client client) {
		this.client = client;
		this.model = new CharacterModel();
	}
	public CharacterDto(Client client, CharacterModel model) {
		this.client = client;
		this.model = new CharacterModel(model);
	}
	
	public CharacterModel getCharacterModel() {
		return this.model;
	}
	public Client getClient() {
		return this.client;
	}
}
