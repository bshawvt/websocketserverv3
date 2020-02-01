package simulator;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


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
	
		// debug to stress node
		//for(int i = 0; i < 2000; i++)
		//	world.addNetObject();
	
		
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
	int spsCount = 0;
	
	int stepSize = 500;
	
	public void update() {
		flushThreadMessages();
		
		double now = System.nanoTime()/1000000;
		
		/*if (profiler.hasElapsed("sps", 1000)) {
			//System.out.println("has elapsed in " + id + " current sps: " + sps);
			sps = spsCount;
			spsCount = 0;
			
			System.out.println("=====================================" +
				"\n node-" + id + " metrics:" +
				"\ntime taken between frames ms: " + profiler.elapse("test1") +
				"\nsteps per second: " + sps +
				"\nnetobject count: " + world.netObjects.size() +
				"\nnetobjects per tree: " + stepSize
			);
			
		}*/
		
		if (now > (dt + (TimeStep))) {
			System.err.println("SKiPPeD frAme");
			dt = now;
			
			// todo: 
		}
		
		profiler.start("test1");
		
		
		while (dt < now) {
			dt += TimeStep;
			world.step(dt);
			spsCount++;
		}
		
		
		profiler.stop("test1");
	}
	
	private void flushThreadMessages() {
		SimulatorThreadMessage msg = null;
		while ((msg = nodeThreadMessage.poll()) != null) {
			int type = msg.getType();
			int from = msg.getFrom();
			System.out.println("Node: Node-" + id + ": received a thread message");
			
			switch (type) {
				
				// integrate client state to the simulation
				case SimulatorThreadMessage.Type.Update:{
					break;
				}
				
				// add client to this simulation
				case SimulatorThreadMessage.Type.Add:{
					//msg.get
					world.addNetObject(msg.getClientId(), msg.getCharacter());
					System.out.println("TODO: Node: Node-" + id + ": added net object to simulation" );
					break;
				}
				
				// remove client from this simulation
				case SimulatorThreadMessage.Type.Remove:{ 
					world.removeNetObject(msg.getClientId());
					System.out.println("TODO: Node: Node-" + id + ": removed net object to simulation" );
					break;
				}
				
				// Nodes do not use None, this should never happen
				case SimulatorThreadMessage.Type.None:{
					break;
				}
				
				// should never happen
				default:{
					break;
				}
			}
		}
	}
}
