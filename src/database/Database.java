package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import Dtos.AuthenticationDto;
import Dtos.CharacterDto;
import Models.CharacterModel;
import Models.UserAccountModel;
import main.Config;
import server.Client;
import server.ServerThreadMessage;
import simulator.SimulatorThreadMessage;
import threads.Threads;

public class Database {
	private Connection connection;
	public Cache cache;//HashMap<Integer, CharacterModel> charactersCache;
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
			initCache();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initCache() {
		this.cache = new Cache();
	}
	
	public void addCharacter(CharacterDto dto) {
		System.out.println("Database: addCharacter:");
		try {
			UserAccountModel user = dto.getClient().getUserAccount();
			System.out.println(user);
			String query = "CALL WSProc_AddCharacter(?);";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, user.getUserId());
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				System.out.println("... addCharacter has succeeded!");
				//Threads.getSimulatorQueue().offer(new SimulatorThreadMessage());
			}
		
		}
		catch (SQLException e) {
			System.err.println("... addCharacter has failed!");
		}
		
	}
	public void consumeSessionToken(AuthenticationDto dto) {
		
		AuthenticationDto ndto = new AuthenticationDto(dto);
		//ndto.setClient(dto.getClient());
		
		try {
			System.out.println("Database: consumeSessionToken: " + dto.getToken());
			String query = "CALL WSProc_ConsumeSessionToken(?, ?);";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, dto.getToken());
			statement.setString(2, dto.getOwnerAddress());
			ResultSet result = statement.executeQuery();
			
			
			while (result.next()) {
			//if (result.next()) { // if a token can be used then construct a new useraccount  for the dto
				//System.out.println("... consumeSessionToken has succeeded!");
				if (dto.getUserAccount() == null) {
					ndto.setUserAccount(new UserAccountModel(result));
					System.out.println("... set  user account model!");
				}
				
				CharacterModel character = new CharacterModel(result);
				if (character.getCharacterId() > 0) {
					// cache for simulator thread
					/*ArrayList<CharacterModel> precache = cache.characters.get(dto.getOwner()); 
					if (precache == null) {
						cache.characters.put(dto.getOwner(), new ArrayList<>());
					}
					cache.characters.get(dto.getOwner()).add(character);*/
					//if (cache.characters.get(dto.getOwner()) == null) {
					//	cache.characters.g
						//cache.characters.get(dto.getOwner()) = new ArrayList<>();
					//}
					//cache.characters.put(dto.getOwner(), );
					
					// prepare dto with list of characters if user has any
					ndto.addCharacter(character);
					System.out.println("... added character model!");
				}
			}
		}
		
		catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("... consumeSessionToken has failed!");
		}
		
		finally {
			Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Database, 
					ServerThreadMessage.Type.Authenticate, 
					ndto));
		}
	}	

}
