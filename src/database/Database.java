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
		/*
		 */
		try {
			//CALL WSProc_ConsumeToken('testuser0IESlZN+0m65yHj3Pd+uxAn3VRxqA2ecT5v5bsD+DNfs=', "127.0.0.1");
			System.out.println("Database: consumeToken:");
			String query = "CALL WSProc_ConsumeToken(?, ?);";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, dto.getToken());
			statement.setString(2, dto.getOwnerAddress());
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				//dto.getUserAccount().setSessionToken(dto.getToken()); // set the token for matching with client
				dto.setUserAccount(new UserAccountModel(result));
				Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Database, ServerThreadMessage.Type.Authenticate, dto));
				return;// new UserAccountModel(result);
			}
			System.out.println("... consumeToken has failed!");
			return;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
