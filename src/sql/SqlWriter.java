package sql;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;
import java.util.Calendar;

import Main.Config;
import dtos.CharacterDto;
import dtos.UserAccountDto;
import util.L;


public class SqlWriter {

	private Connection connection;
	
	public SqlWriter() {

		try {
			this.connection = DriverManager.getConnection(Config.DB_ADDRESS, Config.DB_USER, Config.DB_PASS);
			System.out.println("SqlClient: connected to database server");
		} catch (SQLException e) {
			System.out.println("SqlException thrown");
			System.out.println(e);
		}
	}
	
	
	//public void createAccountRecovery(String email) {
		/*try {
			String query = "INSERT INTO useraccountrecovery VALUES(NULL, ?, ?, ?, NOW())";
			PreparedStatement statement = this.getConnection().prepareStatement(query);
			statement.setString(1, token);
			statement.setLong(2, user
			statement.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}*/
		/*try {
			//Statement statement = this.getConnection().createStatement();
			
			java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			
			byte[] seed = SqlHelper.Seed();
			String salt = Base64.getEncoder().encodeToString(seed);
			
			byte[] hash = null;
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("SHA-256");
				hash = digest.digest((password + salt).getBytes());
				
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return false;
			}

			String query = "INSERT INTO useraccounts VALUES(NULL, NULL, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement statement = this.getConnection().prepareStatement(query);
			statement.setString(1, user);
			statement.setBytes(2, hash);
			statement.setString(3, salt);
			statement.setString(4, email);
			statement.setDate(5, date);
			statement.setLong(6, 0);
			statement.setBoolean(7, false);
			
			statement.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	//}
	
	/*public boolean createAccount(String user, String password, String email) {
		try {
			//Statement statement = this.getConnection().createStatement();
			
			java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			
			byte[] seed = SqlHelper.Seed();
			String salt = Base64.getEncoder().encodeToString(seed);
			
			byte[] hash = null;
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("SHA-256");
				hash = digest.digest((password + salt).getBytes());
				
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return false;
			}

			String query = "INSERT INTO useraccounts VALUES(NULL, NULL, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement statement = this.getConnection().prepareStatement(query);
			statement.setString(1, user);
			statement.setBytes(2, hash);
			statement.setString(3, salt);
			statement.setString(4, email);
			statement.setDate(5, date);
			statement.setLong(6, 0);
			statement.setBoolean(7, false);
			
			statement.executeUpdate();
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("-1");
			return false;
		}
	}*/
	public UserAccountDto getUserAccountOnVerify(String username, String password) {
		
		try {
			//Statement statement = this.getConnection().createStatement();
			//statement.executeQuery("SELECT * FROM useraccounts WHERE username = '" + username + "' AND password = '" + password + "'");
			String query = "SELECT * FROM useraccounts WHERE username = ?;";
			PreparedStatement statement = this.getConnection().prepareStatement(query);
			statement.setString(1, username);
			ResultSet result = statement.executeQuery();
			//result.
			while (result.next()) {

				UserAccountDto userAccount = new UserAccountDto(result);
				
				byte[] hash = null;
				MessageDigest digest;
				try {
					digest = MessageDigest.getInstance("SHA-256");
					hash = digest.digest((password + userAccount.getSalt()).getBytes());
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					return null;
				}
				String phash = new String(userAccount.getHash());
				String thash = new String(hash);
				
				System.out.println("saved hash: " + userAccount.getSalt().getBytes());
				System.out.println("saved hash + salt bytes: " + (phash + userAccount.getSalt()).getBytes());
				System.out.println("saved hash:	" + phash);
				System.out.println("new hash:	" + thash);
				
				if (phash.equals(thash))
				{
					//if (!userAccount.isLocked()) {
						updateLastLoginDate(userAccount);
						return userAccount;
					//}
				}
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public void updateLastLoginDate(UserAccountDto dto) {
		try {
			
			if (dto.getId() > 0) {
				String query = 	"UPDATE useraccounts SET " +
									"lastLoginDate = NOW() " +	// 1
								"WHERE id = ?;"; // 1
				PreparedStatement statement = this.getConnection().prepareStatement(query);
				statement.setLong(1, dto.getId());
				
				statement.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*public boolean isEmailInUse(String email) {
		try {
			String query = "SELECT email FROM useraccounts WHERE email = ?";
			PreparedStatement statement = this.getConnection().prepareStatement(query);
			statement.setString(1, email);
			ResultSet result = statement.executeQuery();
			//Statement statement = this.getConnection().createStatement();
			//statement.executeQuery("SELECT email FROM useraccounts WHERE email = '" + email + "'");
			//ResultSet result = statement.getResultSet();
			if (result.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}*/
	/*public boolean isUsernameInUse(String username) {
		try {
			String query = "SELECT username FROM useraccounts WHERE username = ?";
			PreparedStatement statement = this.getConnection().prepareStatement(query);
			statement.setString(1,  username);
			ResultSet result = statement.executeQuery();
			//Statement statement = this.getConnection().createStatement();
			//statement.executeQuery("SELECT username FROM useraccounts WHERE username = '" + username + "'");
			//ResultSet result = statement.getResultSet();
			if (result.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}*/
	
	public Connection getConnection() {
		return this.connection;
	}
	
	private void close() {
		try {
			this.getConnection().close();
			System.out.println("SqlClient: closed connection to server");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public CharacterDto getCharacterDtoFromOwnerId(long id) {
		
		try {
			String query = "SELECT * FROM characters WHERE owner = ?;";
			PreparedStatement statement = this.getConnection().prepareStatement(query);
			statement.setLong(1, id);
			ResultSet result = statement.executeQuery();
			//result.
			while (result.next()) {

				CharacterDto character = new CharacterDto(result);
				
				return character;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public void insertServerStatus() {
		try {
			String query = "INSERT INTO serverstatus VALUES(NULL, NOW());";
			PreparedStatement statement = this.getConnection().prepareStatement(query);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveCharacterDto(CharacterDto dto) {
		
		try {
			
			if (dto.getId() > 0) {
				String query = 	"UPDATE characters SET " +
									"name = ?, " +	// 1
									"description = ?, " +	// 2
									"x = ?, " +	// 3
									"y = ?, " +	// 4
									"z = ?, " +	// 5
									"wisdomLevel = ?, " +	// 6
									"wisdomPoints = ?, " +	// 7
									"unsafeExperiencePoints = ?, " +	// 8
									"safeExperiencePoints = ?, " +	// 9
									"lifetimeExperiencePoints = ?, " +	// 10
									"maxHealth = ?, " +	// 11
									"maxStamina = ? " +	// 12
									//" = ?, " +
								"WHERE id = ?;";
				PreparedStatement statement = this.getConnection().prepareStatement(query);
	
				statement.setString(1, dto.getName());
				statement.setString(2, dto.getDescription());
	
				statement.setDouble(3, dto.getX());
				statement.setDouble(4, dto.getY());
				statement.setDouble(5, dto.getZ());
				
				statement.setInt(6, dto.getWisdomLevel());
				statement.setInt(7, dto.getWisdomPoints());
				statement.setLong(8, dto.getUnsafeExperiencePoints());
				statement.setLong(9, dto.getSafeExperiencePoints());
				statement.setLong(10, dto.getLifetimeExperiencePoints());
				statement.setInt(11,  dto.getMaxHealth());
				statement.setInt(12, dto.getMaxStamina());
				
				statement.setLong(13, dto.getId());
				
				statement.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void createCharacter(CharacterDto dto) {
		try {

			java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			String query = 	"INSERT INTO characters " + 
							"(owner, name, dateOfCreation) " +
							"VALUES (?, ?, ?);";
			PreparedStatement statement = this.getConnection().prepareStatement(query);
			statement.setLong(1, dto.getOwner());
			statement.setString(2, dto.getName());
			statement.setDate(3, date);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//@Override
	public void run() {
		
		
	}
};
