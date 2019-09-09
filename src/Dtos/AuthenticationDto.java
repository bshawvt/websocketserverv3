package Dtos;

import Models.UserAccountModel;

public class AuthenticationDto {
	
	private UserAccountModel userAccount;
	private String token;
	private long owner;
	private String ownerAddress;

	public AuthenticationDto() {		
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
	public void setOwner(long id) {
		this.owner = id;
	}
	public long getOwner() {
		return this.owner;
	}
	public String getOwnerAddress() {
		return this.ownerAddress;
	}
	public void setOwnerAddress(String address) {
		this.ownerAddress = address;
	}
}
