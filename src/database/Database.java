package database;

import java.sql.*;

import Dtos.AuthenticationDto;
import Models.UserAccountModel;
import database.DatabaseThreadMessage.Type;
import main.Config;
import server.ServerThreadMessage;
import threads.Threads;

public class Database {
	private Connection connection;
	public Database() {
		
		/*
		 *  todo: need to cache data for some records eventually to 
		 *  prevent sending a lot of data to the dbms all the time
		 *  
		 */
		
		try {
			this.connection = DriverManager.getConnection(Config.DatabaseAddress, Config.DatabaseUsername, Config.DatabasePassword);
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
			if (result.next()) { // if a token can be used then construct a new useraccount  for the dto
				System.out.println("... consumeSessionToken has succeeded!");
				dto.setUserAccount(new UserAccountModel(result));
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
