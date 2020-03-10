package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import Dtos.StateChangeDto;
import Models.CharacterModel;
import main.Config;
import server.ServerThreadMessage;
import server.blobs.MessageBlob;
import simulator.netobjects.NetObject;
import simulator.netobjects.Player;
import threads.Threads;
import ui.Form;

public class World {
	
	public ArrayList<NetObject> netObjects = new ArrayList<>();
	public ArrayList<NetObject> netObjectsQueue = new ArrayList<>();
	public long uuidCounter = 0;
	
	//public Metrics metrics = new Metrics();
	private HashMap<Integer, NetObject> clientNetObjects = new HashMap<>();
	public Tree tree = new Tree(netObjects);
	
	public long frameCount = 0; // increased every time a world step has completed
	
	
	//public Tree nodeTree = new Tree<NetObject>();
	
	
	public World() {
		
	}
	
	public void step(double dt) {
		flush();
	
		Iterator<NetObject> it = netObjects.iterator();
		while(it.hasNext()) {
			NetObject netObject = it.next();

				
			netObject.step(this, dt);
			
			NetObject last = netObject.snapshots.last();
			// compare with last state before pushing the next snapshot
			if (compareObjectStates(netObject, last)) {
				System.out.println("cocks?");
				updateOverNetwork(netObject);
			}
			
			if (netObject.isRemoved()) {
				System.out.println("... found and removed a net object:\n\tid " + netObject.getId() + 
						"\n\tclientId " + netObject.getClientId() );
				it.remove();
			}
			
			/* only push a new snapshot after 100 ms have passed 
			 * since they're limited to 10 per */
			if (last == null || dt > last.snapTime + Config.SnapshotDelay)
				netObject.snapshots.push(netObject, dt);
			
			
			
		}
	
		frameCount++;		
		
	}
	/* prepares a frame for sending across the network for synchronizing 
	 * an object with any clients near by */
	public void updateOverNetwork(NetObject who) {
		// list of objects
		ArrayList<NetObject> receivers = tree.getClients(who.position);
		StateChangeDto dto = new StateChangeDto(who, receivers, frameCount);
		Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Simulator, ServerThreadMessage.Type.Update, dto));
		//node.
		
	}
	/* returns true when a variable tied to state has changed */
	public boolean compareObjectStates(NetObject a, NetObject b) {
		if (b == null) // b is only null when the object has no previous snapshots
			return true;
		
		if ((a.inputState.get() != b.inputState.get()) ||
				(a.angles[0] != b.angles[0]) ||
				(a.angles[1] != b.angles[1]) ||
				(a.angles[2] != b.angles[2]) ||
				(a.removed != b.removed) ||
				(a.stateChange != b.stateChange)) {
			return true;
		}
		return false;
	}
	
	/**
	 * adds a clients character to the simulation
	 * @param clientId
	 * @param model
	 */
	public void addNetObject(int clientId, CharacterModel model) {
		
		if (model == null) return;
		Player obj = new Player(model);
		obj.setClientId(clientId);
		obj.setId(uuidCounter++);
		clientNetObjects.put(clientId, obj);
		netObjectsQueue.add(obj);
			
	}
	/**
	 * removes the clients character from the simulation
	 * @param clientId
	 */
	public void removeNetObject(int clientId) {
		NetObject obj = clientNetObjects.get(clientId);//.setRemoved(true);
		if (obj != null) {
			obj.setRemoved(true);
			//obj.removed = true;
			clientNetObjects.remove(clientId);
		}
		else
			System.out.println("ruh roh");
	}
	
	/* merges new objects into state */
	private void flush() {		
		Iterator<NetObject> it1 = netObjectsQueue.iterator();
		while (it1.hasNext()) {
			NetObject netObject = it1.next();
			netObjects.add(netObject);
			System.out.println("... added new netobject:\n\tid " + netObject.getId() + 
					"\n\tclientId " + netObject.getClientId() );
			netObject.stateChange = !netObject.stateChange;
			
			final ArrayList<NetObject> tmpObjs = new ArrayList<>();
			netObjects.forEach((e) -> {
				tmpObjs.add(e);
			});
			Form.UpdateQuadPoints(tmpObjs);
			
		}
		netObjectsQueue.clear();
	}
}
