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
		try {
			this.connection = DriverManager.getConnection(Config.DatabaseAddress, Config.DatabaseUsername, Config.DatabasePassword);
			System.out.println("Database: connected to the database server. using " + Config.DatabaseSchema + " schema");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void consumeToken(AuthenticationDto dto) {
		/* sets useraccount inside of dto on success
		 */
		try {
			System.out.println("Database: consumeToken:");
			String query = "CALL WSProc_ConsumeToken(?, ?);";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, dto.getToken());
			statement.setString(2, dto.getOwnerAddress());
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				// if a token can be used then construct a new useraccount object for the dto
				System.out.println("... consumeToken has succeeded!");
				dto.setUserAccount(new UserAccountModel(result));
				Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Database, 
						ServerThreadMessage.Type.Authenticate, 
						dto));
				return;
			}
			// otherwise just send back the dto as it is
			System.out.println("... consumeToken has failed!");
			Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Database, 
					ServerThreadMessage.Type.Authenticate, 
					dto));
			return;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
