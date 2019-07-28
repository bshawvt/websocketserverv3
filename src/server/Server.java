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

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import main.Config;
import threads.Threads;

public class Server extends WebSocketServer {

	public Server() { //throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException {
		super(new InetSocketAddress(Config.ServerPort));
		this.setReuseAddr(true);
		this.setTcpNoDelay(true);
		
		// uncomment for untested ssl
		/*
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			File kf = new File("E:\\Development\\Projects\\Java\\workspaces\\WebsocketServerV3\\website\\keystore.jks");
			ks.load(new FileInputStream(kf), (new String("apassword")).toCharArray());
			
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, (new String("bpassword")).toCharArray());
			
			TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
			tmf.init( ks );
			
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			
			this.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));
		}
		catch (CertificateException ce) {
			
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
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
