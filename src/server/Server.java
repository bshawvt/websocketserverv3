package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import database.DatabaseThreadMessage;
import main.Config;
import threads.Threads;

public class Server extends WebSocketServer {
	
	public static class Reason {
		public static final int None = 4100;
		public static final int Clean = 4101;
		public static final int FailedAuthStep = 4102;
	}

	public Server() { //throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException {
		super(new InetSocketAddress(Config.ServerPort));
		this.setReuseAddr(true);
		this.setTcpNoDelay(true);
		
		if (Config.UseSSL)// untested with project and doesn't seem to work with self signed certificates
			try {
				System.out.println("using ssl!");
				this.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(new SSL().getSSLContextFromLetsEncrypt()));
			}
			finally {
				
			}
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
		System.out.println(connection + " has closed their connection, code: " + code + ", message: " + message);
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
		System.out.println(connection.getRemoteSocketAddress().getHostString() + " has connected");

		if (handshake.getResourceDescriptor().length() > 10) {
			Threads.getDatabaseQueue().offer(new DatabaseThreadMessage(Threads.Server, DatabaseThreadMessage.Type.Auth, connection.getResourceDescriptor()));
			Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Server, ServerThreadMessage.Type.Set, connection));
		}
		else {
			//connection.close();//closeConnection(100, "u suck");
			connection.close(Reason.FailedAuthStep, "Invalid auth token");
		}
	}
}
