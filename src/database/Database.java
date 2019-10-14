package database;

import java.sql.*;
import java.util.HashMap;

import Dtos.AuthenticationDto;
import Models.CharacterModel;
import Models.UserAccountModel;
import database.DatabaseThreadMessage.Type;
import main.Config;
import server.ServerThreadMessage;
import threads.Threads;

public class Database {
	private Connection connection;
	public HashMap<Integer, CharacterModel> charactersCache;
	/*
	 * cache [{characters}]
	 */
	public Database() {
		
		/*
		 *  todo: need to cache data for some records eventually to 
		 *  prevent sending a lot of data to the dbms all the time
		 *  
		 */
		
		try {
			this.connection = DriverManager.getConnection(Config.getConnectionString(), Config.DatabaseUsername, Config.DatabasePassword);
			System.out.println("Database: connected to the database server. using " + Config.DatabaseSchema + " schema");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void consumeSessionToken(AuthenticationDto dto) {
		try {
			System.out.println("Database: consumeSessionToken:");
			String query = "CALL WSProc_ConsumeSessionToken(?, ?);";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, dto.getToken());
			statement.setString(2, dto.getOwnerAddress());
			ResultSet result = statement.executeQuery();
			while (result.next()) {
			//if (result.next()) { // if a token can be used then construct a new useraccount  for the dto
				//System.out.println("... consumeSessionToken has succeeded!");
				if (dto.getUserAccount() == null) {
					dto.setUserAccount(new UserAccountModel(result));
					System.out.println("... set  user account model!");
				}
				CharacterModel character = new CharacterModel(result);
				charactersCache.put(dto.getOwner(), character);
				dto.addCharacter(character);
				System.out.println("... added character model!");
			}
		}
		
		catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("... consumeSessionToken has failed!");
		}
		
		finally {
			Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Database, 
					ServerThreadMessage.Type.Authenticate, 
					dto));
		}
	}
	

}
