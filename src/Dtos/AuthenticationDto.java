package Dtos;

import java.util.ArrayList;

import Models.CharacterModel;
import Models.UserAccountModel;
import server.Client;

public class AuthenticationDto {
	
	private UserAccountModel userAccount;
	private ArrayList<CharacterModel> characters;
	private Client client;
	
	private String token;
	private String ownerAddress;
	private String error;
	
	public AuthenticationDto() {
		this.characters = new ArrayList<>();
		this.userAccount = null;
		this.client = null;
	}
	public UserAccountModel getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(UserAccountModel model) {
		this.userAccount = model;
	}
	public String getToken() {
		return this.token;
	}
	public void setToken(String token) {
		if (token.startsWith("/")) // remove the / if it exists
			token = token.substring(1);
		this.token = token;
	}

	public String getOwnerAddress() {
		return this.ownerAddress;
	}
	public void setOwnerAddress(String address) {
		this.ownerAddress = address;
	}
	public Client getClient() {
		return this.client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	/* contains character select data only */
	public void addCharacter(CharacterModel model) {
		this.characters.add(model);
	}
	public ArrayList<CharacterModel> getCharacters() { return this.characters; }
}
