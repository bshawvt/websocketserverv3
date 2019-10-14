package Dtos;

import java.util.ArrayList;

import Models.CharacterModel;
import Models.UserAccountModel;

public class AuthenticationDto {
	
	private UserAccountModel userAccount;
	private ArrayList<CharacterModel> characters;
	
	private int owner;
	
	private String token;
	private String ownerAddress;
	private String error;
	
	public AuthenticationDto() {
		this.characters = new ArrayList<>();
		this.userAccount = null;
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
	public void setOwner(int id) {
		this.owner = id;
	}
	public int getOwner() {
		return this.owner;
	}
	public void setError(String msg) {
		this.error = msg;
	}
	public String getError() {
		return this.error;
	}
	public String getOwnerAddress() {
		return this.ownerAddress;
	}
	public void setOwnerAddress(String address) {
		this.ownerAddress = address;
	}
	public void addCharacter(CharacterModel model) {
		this.characters.add(model);
	}
	public ArrayList<CharacterModel> getCharacters() { return this.characters; }
}
