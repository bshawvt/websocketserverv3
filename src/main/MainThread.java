package main;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import Models.CharacterModel;
import database.DatabaseThread;
import database.DatabaseThreadMessage;

import server.ServerThread;
import server.ServerThreadMessage;
import simulator.SimulatorThread;
import simulator.SimulatorThreadMessage;
import simulator.netobjects.NetObject;
import simulator.netobjects.Player;
import threads.Threads;
import ui.Form;

public class MainThread {

	public static void main(String[] args) {
		new Config(args);
		new Threads(); // assign queues to the main thread cuz i dont know
		Form ui = new Form();
		ui.display(true);
		
		System.out.println("MainThread: Hello world!");

		Thread server = new Thread(new ServerThread(), "Server-0");
		server.setDaemon(false);
		server.start();
		
		Thread database = new Thread(new DatabaseThread(), "Database-0");
		database.setDaemon(false);
		database.start();
		
		Thread simulator = new Thread(new SimulatorThread(), "Simulator-0");
		simulator.setDaemon(false);
		simulator.start();
		
		Scanner in = new Scanner(System.in);
		while(true) { // process user input
			if (in.hasNext()) {
				String input = in.nextLine();
				String[] split = input.split("\\.");
				if (input.equals("exit") || input.equals("end") || input.equals("stop") || input.equals("quit")) {
					//simulator.end();
					//server.end();
					break;
				}
				else if (split[0].equals("help")) {
					System.out.println("exit|end|stop|quit - safely shutdown the server\n" + 
										"srv|sim|db.<command|help> - here be dragons\n" + 
										"");
				}
				else if (split[0].equals("gui")) {
					if (split.length > 1)
						if (split[1].equals("hide")) {
							ui.display(false);
							continue;
						}
					ui.display(true);
				}
				else if (split[0].equals("testquad")) {
					final ArrayList<NetObject> objs = new ArrayList<>();
					objs.add(new Player(new CharacterModel(0)));
					objs.add(new Player(new CharacterModel(0)));
					objs.add(new Player(new CharacterModel(0)));
					Form.UpdateQuadPoints(objs);
				}
				else if (split[0].equals("srv")) {
					Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Main, ServerThreadMessage.Type.None, split[1]));
					
				}
				else if (split[0].equals("sim")) {
					Threads.getSimulatorQueue().offer(new SimulatorThreadMessage(Threads.Main, SimulatorThreadMessage.Type.None, split[1]));
				}
				else if (split[0].equals("db")) {
					Threads.getDatabaseQueue().offer(new DatabaseThreadMessage(Threads.Main, DatabaseThreadMessage.Type.None, split[1]));
				}
				else {
					System.out.println("MainThread: unknown command");
				}
				
				
			}
		}
		in.close();
		
		// thread cleanup
		server.interrupt();
		database.interrupt();
		simulator.interrupt();
		
		
		System.out.println("MainThread: Goodbye world!");
		System.exit(1);
	}

}

