package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import main.Config;
import threads.Threads;

public class Server extends WebSocketServer {

	public Server() {
		super(new InetSocketAddress(Config.ServerPort));
		this.setReuseAddr(true);
		this.setTcpNoDelay(true);
		System.out.println("Server: bound to port " + Config.ServerPort);
	}
	
	@Override
	public void onStart() {
		System.out.println("Server: has started");
		
	}
	
	@Override
	public void stop() {
		try {
			super.stop();
			// todo: shutdown 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClose(WebSocket connection, int code, String message, boolean remote) {
		// TODO Auto-generated method stub
		System.out.println(connection + " has closed their connection");
	}

	@Override
	public void onError(WebSocket connection, Exception exception) {
		// TODO Auto-generated method stub
		System.out.println(connection + " has an error");
		System.out.println(exception);
	}

	@Override
	public void onMessage(WebSocket connection, String message) {
		System.out.println(connection + " sent: " + message);
		
	}

	@Override
	public void onOpen(WebSocket connection, ClientHandshake handshake) {
		System.out.println(connection + " has connected");
		Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Server, ServerThreadMessage.Type.Set, connection));
	}
}
