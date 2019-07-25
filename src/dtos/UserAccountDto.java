package dtos;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccountDto {

	private long id;
	public long getId() { return id; };
	public void setId(long v) { id = v; };
	
	private String username;
	public String getUsername() { return username; };
	public void setUsername(String name) { username = name; };

	private byte[] hash;
	public byte[] getHash() { return hash; };
	public void setHash(byte[] p) { hash = p; };
	
	private String salt;
	public String getSalt() { return salt; };
	public void setSalt(String s) { salt = s; };
	
	private String email;
	public String getEmail() { return email; };
	public void setEmail(String m) { email = m; };
	
	private Date dateOfCreation;
	public Date getDateOfCreation() { return dateOfCreation; };
	public void setDateOfCreation(Date date) { dateOfCreation = date; };
	
	private Date lastLoginDate;
	public Date getLastLoginDate() { return lastLoginDate; };
	public void setLastLoginDate(Date date) { lastLoginDate = date; };
	
	private long permission;
	public long getPermission() { return permission; };
	public void setPermission(long v) { permission = v; };
	
	private boolean locked;
	public boolean isLocked() { return locked; };
	public void setLocked(boolean v) { locked = v; };
	
	private String tmpData;
	public String getTmpData() { return tmpData; };
	public void setTmpData(String data) { tmpData = data; };
	
	public UserAccountDto() {
		
	}
	
	public UserAccountDto(ResultSet result) throws SQLException {
		setId(result.getLong(1));
		setLastLoginDate(result.getDate(2));
		setUsername(result.getString(3));
		setHash(result.getBytes(4));
		setSalt(result.getString(5));
		setEmail(result.getString(6));
		setDateOfCreation(result.getDate(7));
		setPermission(result.getLong(8));
		setLocked(result.getBoolean(9));
		setTmpData("");
	}
	
	public UserAccountDto(UserAccountDto user) {
		setId(user.getId());
		setLastLoginDate(user.getLastLoginDate());
		setUsername(user.getUsername());
		setHash(user.getHash());
		setSalt(user.getSalt());
		setEmail(user.getEmail());
		setDateOfCreation(user.getDateOfCreation());
		setPermission(user.getPermission());
		setLocked(user.isLocked());
		setTmpData("");
	}
}
