package Models;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserAccountModel {
	
	private long user_id = -1;
	private String username = null;
	private String combinedHash = null;
	private String salt = null;
	private String email = null;
	private int permissions = 0;
	private boolean locked = true;
	private Date dateOfCreation = null;
	private Date lastLoginDate = null;
	//private String sessionIp = null;
	//private Date sessionExpirationDate = null;
	//private String sessionToken = null;
		
	public UserAccountModel(ResultSet set) throws SQLException {
		this.user_id = set.getLong("user_id");
		this.username = set.getString("username");
		this.combinedHash = set.getString("combinedHash");
		this.salt = set.getString("salt");
		this.email = set.getString("email");
		this.permissions = set.getInt("permissions");
		this.locked = set.getBoolean("locked");
		this.dateOfCreation = set.getDate("dateOfCreation");
		this.lastLoginDate = set.getDate("lastLoginDate");
		/*this.user_id = set.getLong(1);
		this.username = set.getString(2);
		this.combinedHash = set.getString(3);
		this.salt = set.getString(4);
		this.email = set.getString(5);
		this.permission = set.getInt(6);
		this.locked = set.getBoolean(7);
		this.dateOfCreation = set.getDate(8);
		this.lastLoginDate = set.getDate(9);*/
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
	public int getPermissions() {
		return this.permissions;
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
