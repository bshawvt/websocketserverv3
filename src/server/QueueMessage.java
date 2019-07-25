package server;

import org.java_websocket.WebSocket;

public class QueueMessage {
	private WebSocket connection;
	private String message;
	public QueueMessage(WebSocket connection, String message) {
		setConnection(connection);
		setMessage(message);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public WebSocket getConnection() {
		return connection;
	}
	public void setConnection(WebSocket connection) {
		this.connection = connection;
	}
}
