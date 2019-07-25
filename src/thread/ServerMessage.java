package thread;

import org.java_websocket.WebSocket;

public class ServerMessage {

	private final WebSocket connection;
	private final String message;
	
	public ServerMessage(WebSocket connection, String message) {
		this.connection = connection;
		this.message = message;
	}

	public WebSocket getConnection() {
		return connection;
	}

	public String getMessage() {
		return message;
	}

	
	
}
