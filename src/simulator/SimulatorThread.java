package simulator;

import java.util.ArrayList;
import java.util.HashMap;

import Dtos.CharacterDto;
import Dtos.StateChangeDto;

import database.DatabaseThreadMessage;
import main.Config;
import server.Client;
import threads.Threads;
import tools.Profiler;
import ui.Form;

public class SimulatorThread implements Runnable {
	
	private boolean isRunning;

	private Node[] nodes = new Node[Config.NodeLimit];
	private int nodeCount = 0; // current number of active nodes	

	private HashMap<Integer, Node> clientLocation; // which node each server client belongs to
	
	public SimulatorThread() {
		this.isRunning = false;
		System.out.println("SimulatorThread: Hello world!");
		
		this.clientLocation = new HashMap<>();
		
		// create the default node
		spawnNode();
		
	}
	
	@Override
	public void run() {
		// TODO: 
		propagateSimulatorThreadMessages();
		
	}
	private void processCommand(String data, SimulatorThreadMessage msg) {
		String[] split = data.split(" ");
		String command = split[0];
		if (command.equals("help")) {
			System.out.println("you have been helped");
		}
		else if (command.equals("get_view") && split.length > 1) {
			int i = Integer.parseInt(split[1]);
			if (nodes[i] != null) {
				nodes[i].nodeThreadMessage.offer(msg);
			}
		}
		else if (command.equals("kick")) {
			
		}
		else {
			System.out.println("SimulatorThread: processCommand: unknown command:\n... " + command);
		}
	}
	private void propagateSimulatorThreadMessages() {

		try {
			SimulatorThreadMessage msg = null;
			while ((msg = Threads.getSimulatorQueue().take()) != null) {
				int type = msg.getType();
				int from = msg.getFrom();
				
				//Node node = getClientNode(msg.getClientId());//this.nodes[0];//getNodeByClientLocation(msg.getClientId());
				
				switch (type) {
				
					// input processing
					case SimulatorThreadMessage.Type.None: {
						if (from == Threads.Main) {
							String command = msg.getCommand();
							if (command != null) {
								System.out.println("SimulatorThread: received command");
								System.out.println("command: " + command);
								processCommand(command, msg);
							}
						}
						break;
					}
					
					// integrate client state with node
					case SimulatorThreadMessage.Type.Update: {
						System.out.println("SimulatorThread: received an Update event");
						if (from == Threads.Server) {
							StateChangeDto dto = (StateChangeDto) msg.getDto();
							Node node = getClientNode(dto.getClientId());
							//if (node == null) continue; // should never happen but just in case, skip 
							System.out.println("... from Server!");
							System.out.println("... update sent to node: " + node.id); 
							node.nodeThreadMessage.offer(msg);
						}
						break;
					}
					
					// add a new client to the simulation
					case SimulatorThreadMessage.Type.Add: {
						System.out.println("SimulatorThread: received an Add event");
						if (from == Threads.Server) {

							// choose the best node to place this new user
							Node node = getBestNode();
							System.out.println(node);
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
							
							clientLocation.put(dto.getClientId(), node);
							node.nodeThreadMessage.offer(msg);

						}
						break;
					}
					
					// remove a client from the simulation
					case SimulatorThreadMessage.Type.Remove: {
						System.out.println("SimulatorThread: received an Remove event");
						if (from == Threads.Server) {
							
							CharacterDto dto = (CharacterDto) msg.getDto();
							Node node = getClientNode(dto.getClientId());
							
							if (node == null) {
								System.err.println("... node is null\n"
										+ "\nclientId: " + dto.getClientId()
										+ "\nclientLocations: " + clientLocation.size());
								continue; // should never happen but just in case, skip 
							}
							
							System.out.println("... from Server!");
							System.out.println("... removed character from node: " + node.id);
							
							clientLocation.remove(dto.getClientId());
							node.nodeThreadMessage.offer(msg);
						}
						break;
					}

					default: {
						break;
					}
					
				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* returns the node the user is currently in */
	private Node getClientNode(int id) {		
		return clientLocation.get(id);
	}
	
	/* chooses the first empty node available or defaults to 0 */
	private Node getBestNode() {
		for(int i = 0; i < Config.NodeLimit; i++) {
			if (this.nodes[i] != null && this.nodes[i].getObjectsCount() < Config.NodeSize) {
				System.out.println("getBestNode: chose " + i);
				return this.nodes[i];
			}
		}
		return this.nodes[0];
	}
	
	/* creates a new node thread and increases the node counter */
	private Node spawnNode() {
		if (this.nodeCount < Config.NodeLimit) {		
			Node node = new Node(this.nodeCount);
			this.nodes[this.nodeCount] = node;
			
			Thread nodeThread = new Thread(node, "Node-" + this.nodeCount);
			nodeThread.setDaemon(false);
			nodeThread.start();
			this.nodeCount++;
			
			return node;
		}
		return null;
	}
}
