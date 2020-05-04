package simulator;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import Dtos.CharacterDto;
import Dtos.StateChangeDto;
import Models.CharacterModel;
import jdk.nashorn.internal.runtime.doubleconv.DtoaBuffer;
import server.ServerThreadMessage;
import shared.Bitfield;
import simulator.sceneobjects.SceneObject;
import simulator.sceneobjects.ScenePlayer;
import threads.Threads;
import tools.Profiler;
import ui.Form;

/* nodes are threads which get spawned by simulator thread */
public class Node implements Runnable {
	

	public boolean isRunning = false; 
	public int id = 0; // node id is used just for visual representation 
		
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
		
		
		this.id = id;
		System.out.println("Node-" + id + ": Hello world!");
	
		// debug to stress node
		//for(int i = 0; i < 2000; i++)
		//	world.addNetObject();
	
		
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
	private void processCommand(String data) {
		String[] split = data.split(" ");
		String command = split[0];
		if (command.equals("get_view")) {
			Form.UpdateQuadPoints(world.sceneObjects);
		}
		else {
			System.out.println("SimulatorThread: Node: processCommand: unknown command:\n... " + command);
		}
	}
	private void flushThreadMessages() {
		SimulatorThreadMessage msg = null;
		while ((msg = nodeThreadMessage.poll()) != null) {
			int type = msg.getType();
			int from = msg.getFrom();
			System.out.println("Node: Node-" + id + ": received a thread message");
			
			switch (type) {
				
				// client state to the simulation
				case SimulatorThreadMessage.Type.Update:{
					StateChangeDto dto = (StateChangeDto) msg.getDto();
					SceneObject client = world.getClientSceneObject(dto.clientId);
					System.out.println(dto.inputState);
					client.inputState = new Bitfield(dto.inputState);
					client.yaw = dto.angles[0];
					client.pitch = dto.angles[1];
					client.roll = dto.angles[2];
					
					break;
				}
				
				// add client to this simulation
				case SimulatorThreadMessage.Type.Add:{
					//msg.get
					//world.addNetObject(msg.getClientId(), msg.getCharacter());
					CharacterDto dto = (CharacterDto) msg.getDto();//).getClient().getId()
					System.out.println("TODO: Node: Node-" + id + ": added net object to simulation for client " + dto.getClient().getId());
					world.addSceneObject(dto.getClient().getId(), dto.getCharacterModel());
					//Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Simulator, ServerThreadMessage.Type.Update, dto));
					break;
				}
				
				// remove client from this simulation
				case SimulatorThreadMessage.Type.Remove:{
					CharacterDto dto = (CharacterDto) msg.getDto();//).getClient().getId()
					System.out.println("TODO: Node: Node-" + id + ": removed net object for client " + dto.getClient().getId());
					world.removeSceneObject(dto.getClient().getId());
					break;
				}
				

				case SimulatorThreadMessage.Type.None:{
					processCommand(msg.getCommand());
					break;
				}
				
				// should never happen
				default:{
					break;
				}
			}
		}
	}
	
	public long getObjectsCount() {
		return this.world.sceneObjects.size();
	}
}
