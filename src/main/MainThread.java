package main;

import java.util.Scanner;

import server.ServerThread;
import server.ServerThreadMessage;
import threads.Threads;

public class MainThread {

	public static void main(String[] args) {
		if (args.length == 4) new Config(args);
		new Threads(); // assign queues to the main thread cuz i dont know
		
		System.out.println("MainThread: Hello world!");
	
		
		Thread server = new Thread(new ServerThread(), "Server-0");
		server.setDaemon(false);
		server.start();
		
		Scanner in = new Scanner(System.in);
		while(true) { // process user input
			if (in.hasNext()) {
				String input = in.nextLine();
				String[] split = input.split("\\.");
				if (input.equals("exit") || input.equals("end") || input.equals("stop") || input.equals("quit")) {
					break;
				}
				else if (split[0].equals("help")) {
					System.out.println("exit|end|stop|quit - safely shutdown the server\n" + 
										"srv|sim|db.<command> - here be dragons\n");
				}
				else if (split[0].equals("srv")) {
					Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Main, ServerThreadMessage.Type.Set, split[1]));
					
				}
				else if (split[0].equals("sim")) {
					System.out.println("todo: sim command");
				}
				else if (split[0].equals("db")) {
					System.out.println("todo: db command");
				}
				
				
			}
		}
		in.close();
		
		// thread cleanup
		server.interrupt();
		
		
		System.out.println("MainThread: Goodbye world!");
		System.exit(1);
	}

}

