package simulator;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import Models.CharacterModel;
import server.Clients;
import threads.Threads;
import tools.Profiler;

public class Node implements Runnable {
	

	public boolean isRunning = false; // if this node is being 
	public int id = 0;
	
	public ArrayList<Integer> clients;
	
	private double dt = 0;
	private static final double TimeStep = 1000/30;
	
	private World world;
	private Profiler profiler;
	
	public LinkedBlockingQueue<SimulatorThreadMessage> nodeThreadMessage;
	

	public Node(int id) {
		this.world = new World();
		this.profiler = new Profiler();
		
		this.dt = System.nanoTime()/1000000;
		this.nodeThreadMessage = new LinkedBlockingQueue<>();
		
		this.clients = new ArrayList<>();
		
		this.id = id;
		System.out.println("Node-" + id + ": Hello world!");
	}
	
	public boolean hasClient(int id) {
		return false;
	}

	@Override
	public void run() {
		isRunning = true;
		while(isRunning) {
			update();
		}
	}
	
	int sps = 0; 
	public void update() {
		flushThreadMessages();
		
		double now = System.nanoTime()/1000000;
		
		if (profiler.hasElapsed("sps", 1000)) {
			//System.out.println("has elapsed in " + id + " current sps: " + sps);
			sps = 0;
		}
		
		while (dt < now) {
			dt += TimeStep;
			world.step(dt);
			sps++;
			//System.out.println((long)dt);
		}
	}
	
	private void flushThreadMessages() {
		SimulatorThreadMessage msg = null;
		while ((msg = nodeThreadMessage.poll()) != null) {
			int type = msg.getType();
			int from = msg.getFrom();
			System.out.println("Node: Node-" + id + ": received a thread message");
			
			switch (type) {
				// Nodes do not use None, this should never happen
				case SimulatorThreadMessage.Type.None:{
					break;
				}
				
				// integrate client state with this simulation
				case SimulatorThreadMessage.Type.Update:{
					break;
				}
				
				// add client to this simulation
				case SimulatorThreadMessage.Type.Add:{
					//msg.get
					world.addNetObject(msg.getClientId(), msg.getCharacter());
					System.out.println("Node: Node-" + id + ": added net object to simulation" );
					break;
				}
				
				// remove client from this simulation
				case SimulatorThreadMessage.Type.Remove:{ 
					world.removeNetObject(msg.getClientId());
					break;
				}
				
				// should never happen
				default:{
					break;
				}
			}
			
			//int node = getNode(msg.getClientId());
			/*
			switch (type) {
				case SimulatorThreadMessage.Type.None: {
					if (from == Threads.Main) {
						String command = msg.getCommand();
						if (command != null) {
							System.out.println("Node: received command from server");
							System.out.println("command: " + command);
						}
					}
					break;
				}
				case SimulatorThreadMessage.Type.Update: {
					System.out.println("SimulatorThread: received an Update event");
					if (from == Threads.Server) {
						System.out.println("... from Server!");
						System.out.println("... update sent to node: ???");
					}
					break;
				}
				case SimulatorThreadMessage.Type.Add: {
					System.out.println("SimulatorThread: received an Add event");
					if (from == Threads.Server) {
						System.out.println("... from Server!");
						if (msg.getCharacter() == null) {
							//Threads.getDatabaseQueue().offer(new DatabaseThreadMessage)
						}
						else {
							//new NetObject(world[0], msg.getCharacter());
							System.out.println("... added character to node: ???");
						}
					}
					break;
				}
				case SimulatorThreadMessage.Type.Remove: {
					System.out.println("SimulatorThread: received an Remove event");
					if (from == Threads.Server) {
						System.out.println("... from Server!");
					}
					break;
				}
				default: {
					break;
				}
			
			}*/
		}
	}
}
