package Dtos;

import Models.UserAccountModel;

public class AuthenticationDto {
	
	// ONLY set by database thread!!
	private UserAccountModel userAccount;
	// ONLY set by server thread!!
	private String token;
	private long owner;
	private String ownerAddress;
	private String error;
	
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
}
