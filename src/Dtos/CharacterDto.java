package Dtos;

import Models.CharacterModel;
import server.Client;

public class CharacterDto {
	private CharacterModel model;
	private Client client; // user this character dto was created for
	public int clientId;
	
	public CharacterDto() {
		
	}
	public CharacterDto(Client client) {
		this.client = client;
		this.model = null;//new CharacterModel();
		this.clientId = client.getId();
	}
	public CharacterDto(Client client, CharacterModel model) {
		this.client = client;
		this.model = new CharacterModel(model);
		this.clientId = client.getId();
	}
	public CharacterDto(Client client, long ownerId) {
		this.client = client;
		this.model = new CharacterModel(ownerId);
		this.clientId = client.getId();
	}
	
	public CharacterModel getCharacterModel() {
		return this.model;
	}
	public Client getClient() {
		return this.client;
	}
	public int getClientId() {
		return this.clientId;
	}
}
