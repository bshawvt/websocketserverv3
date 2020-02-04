package simulator;

import java.util.ArrayList;
import java.util.HashMap;

import Dtos.CharacterDto;
import database.DatabaseThreadMessage;
import main.Config;
import threads.Threads;
import tools.Profiler;

public class SimulatorThread implements Runnable {
	
	private boolean isRunning;

	private Node[] nodes = new Node[Config.NodeLimit];
	private Node mainNode;
	

	private HashMap<Integer, Integer> clientLocation; // which node each server client belongs to 

	
	public SimulatorThread() {
		this.isRunning = false;
		System.out.println("SimulatorThread: Hello world!");
		
		this.clientLocation = new HashMap<>();
		
		//for(int i = 0; i < Config.NodeLimit; i++) {
			this.nodes[0] = new Node(1);
			Thread nodeThread = new Thread(this.nodes[0], "Node-" + this.nodes[0].id);
			nodeThread.setDaemon(false);
			nodeThread.start();
		//}
			
		/*this.start = System.nanoTime()/1000000;
		this.dt = this.start;
		this.stepTime = this.start;
		this.world[0] = new World();*/
		
	}
	
	@Override
	public void run() {

		propagateSimulatorThreadMessages();
		/*while(true) {
			
			// distribute messages to the correct nodes
			//propagateSimulatorThreadMessages();
			//if (nodes[0] != null && !nodes[0].isRunnable) { // the first element should be the simulator thread node, if it exists
			//	nodes[0].update();
			//}
			//mainNode.flushThreadMessages();
			//mainNode.update();
		}*/
		
	}
	/**
	 * todo: choose the best node to place a client. 
	 * this is used when a new client has been added to the simulation
	 * @return 0
	 */
	private int getBestNode() {
		int lowest = 0xffffff;
		int current = 0xffffff;
		int best = 0;
		clientLocation.forEach( (Integer e, Integer v) -> {
			System.out.println( "client ID: " + e + ", Node id: " + v);
		});
		return best;
	}
	/**
	 * gets a players location in a simulator node and chooses the best node if the client is new 
	 * @param clientId
	 * @return a node client id belongs to
	 */
	private Node getNodeByClientLocation(int clientId) {
		Integer clientNodeLocation = clientLocation.get(clientId);
		// if client is a new connection then add it to clientLocations 
		if (clientNodeLocation == null) {
			System.out.println("SimulatorThread: getNodeByClientLocation: new client, putting them some where decent i guess");
			int best = getBestNode();
			clientLocation.put(clientId, best);
			clientNodeLocation = best;
		}
		
		Node node = null;
		if ((clientNodeLocation >= 0 && clientNodeLocation < Config.NodeLimit) && (nodes[clientNodeLocation] != null))
			node = nodes[clientNodeLocation];//.nodeThreadMessage.offer(msg);
		
		//System.out.println("SimulatorThread: propagateSimulatorThreadMessage: node.id=" + node.id);
		return node;
	}
	private void propagateSimulatorThreadMessages() {

		try {
			SimulatorThreadMessage msg = null;
			while ((msg = Threads.getSimulatorQueue().take()) != null) {
				int type = msg.getType();
				int from = msg.getFrom();
				
				Node node = this.nodes[0];//getNodeByClientLocation(msg.getClientId());
				
				switch (type) {
					// input processing
					case SimulatorThreadMessage.Type.None: {
						if (from == Threads.Main) {
							String command = msg.getCommand();
							if (command != null) {
								System.out.println("SimulatorThread: received command from server");
								System.out.println("command: " + command);
							}
						}
						break;
					}
					
					// integrate client state with node
					case SimulatorThreadMessage.Type.Update: {
						System.out.println("SimulatorThread: received an Update event");
						if (from == Threads.Server) {
							if (node == null) continue; // should never happen but just in case, skip 
							System.out.println("... from Server!");
							System.out.println("... update sent to node: " + node.id); 

						}
						break;
					}
					
					// add a new client to the simulation
					case SimulatorThreadMessage.Type.Add: {
						System.out.println("SimulatorThread: received an Add event");
						if (from == Threads.Server) {
							
							if (node == null) continue; // should never happen but just in case, skip 
							
							System.out.println("... from Server!");
							CharacterDto dto = (CharacterDto) msg.getDto();
							if (dto.getCharacterModel() != null) {
								System.out.println("... added character to node: " + node.id);
								System.out.println("... character added:\n\tid - " + dto.getCharacterModel().getCharacterId() + 
										"\n\townerid - " + dto.getCharacterModel().getCharacterOwner());
							}
							else {
								System.err.println("... something went wrong!!");
								break;
							}
							node.nodeThreadMessage.offer(msg);

						}
						break;
					}
					
					// remove a client from the simulation
					case SimulatorThreadMessage.Type.Remove: {
						System.out.println("SimulatorThread: received an Remove event");
						if (from == Threads.Server) {
							
							if (node == null) continue; // should never happen but just in case, skip 
							
							System.out.println("... from Server!");
							System.out.println("... removed character from node: " + node.id);
							node.nodeThreadMessage.offer(msg);
						}
						break;
					}
					
					// so
					
					// ???
					default: {
						break;
					}
					
				}
			
				
				/*Integer clientNodeLocation = clientLocation.get(msg.getClientId());
				// client is a new connection
				if (clientNodeLocation == null) {
					int best = getBestNode();
					clientLocation.put(msg.getClientId(), best);
					clientNodeLocation = best;
				}
				
				if ((clientNodeLocation >= 0 && clientNodeLocation < Config.NodeLimit) && (nodes[clientNodeLocation] != null))
					nodes[clientNodeLocation].nodeThreadMessage.offer(msg);*/
				/*if (clientNodeLocation == 0) {
					System.out.println("SimulatorThread: simulator thread message belongs to a user in the main node");
					
					switch (type) {
						case SimulatorThreadMessage.Type.None: {
							if (from == Threads.Main) {
								String command = msg.getCommand();
								if (command != null) {
									System.out.println("SimulatorThread: received command from server");
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
					
					}
				}
				else {
					if (clientNodeLocation >= 0 && clientNodeLocation < Config.NodeLimit)
						nodes[clientNodeLocation].nodeThreadMessage.offer(msg);
				}*/
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
