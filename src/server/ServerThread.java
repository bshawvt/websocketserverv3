package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.LinkedBlockingQueue;

import threads.Threads;

public class ServerThread implements Runnable {
	
	//private final LinkedBlockingQueue<ServerThreadMessage> messageServerQueue;
	public ServerThread() {
		// TODO Auto-generated constructor stub
		//this.messageServerQueue = serverMessageQueue;
		System.out.println("ServerThread: Hello world!");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Server server;

		server = new Server();
		server.start();
		
		
		//server.stop();
		
		while(true) {
			try {
				Thread.sleep(1);
				
				ServerThreadMessage msg = null;
				while ((msg = Threads.getServerQueue().poll()) != null) { // flush thread messages and do things here
					if (msg.getFrom() == Threads.Main) {
						if (msg.getCommand().equals("help")) {
							System.out.println("todo");
						}
					}
					else if (msg.getFrom() == Threads.Simulator) {
						// todo: 
					}
					else if (msg.getFrom() == Threads.Database) {
						// todo:
					}
				}
				
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
