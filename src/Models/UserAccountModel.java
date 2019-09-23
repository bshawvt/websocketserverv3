package Models;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccountModel {
	
	private long user_id = -1;
	private String username = null;
	private String combinedHash = null;
	private String salt = null;
	private String email = null;
	private int permission = 0;
	private boolean locked = true;
	private Date dateOfCreation = null;
	private Date lastLoginDate = null;
	//private String sessionIp = null;
	//private Date sessionExpirationDate = null;
	//private String sessionToken = null;
		
	public UserAccountModel(ResultSet set) throws SQLException {
		this.user_id = set.getLong(1);
		this.username = set.getString(2);
		this.combinedHash = set.getString(3);
		this.salt = set.getString(4);
		this.email = set.getString(5);
		this.permission = set.getInt(6);
		this.locked = set.getBoolean(7);
		this.dateOfCreation = set.getDate(8);
		this.lastLoginDate = set.getDate(9);
		//this.sessionIp = set.getString(10);
		//this.sessionExpirationDate = set.getDate(11);
		//this.sessionToken = set.getString(12);

	}
	
	public long getUserId() {
		return this.user_id;
	}
	public String getUsername() {
		return this.username;
	}
	public String getCombinedHash() {
		return this.combinedHash;
	}
	public String getSalt() {
		return this.salt;
	}
	public String getEmail() {
		return this.email;
	}
	public int getPermission() {
		return this.permission;
	}
	public boolean isLocked() {
		return this.locked;
	}
	public Date getDateOfCreation() {
		return this.dateOfCreation;
	}
	public Date getLastLoginDate() {
		return this.lastLoginDate;
	}
	/*public String getSessionIp() { 
		return this.sessionIp;
	}
	public Date getSessionExpirationDate() {
		return this.sessionExpirationDate;
	}
	public String getSessionToken() {
		return this.sessionToken;
	}*/
}
