package simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Models.CharacterModel;
import server.Client;
import simulator.netobjects.NetObject;
import simulator.netobjects.Player;
import tools.Metrics;

public class World {
	
	public ArrayList<NetObject> netObjects = new ArrayList<>();
	public ArrayList<NetObject> netObjectsQueue = new ArrayList<>();
	public long uuidCounter = 0;
	
	//public Metrics metrics = new Metrics();
	private HashMap<Integer, NetObject> clientNetObjects = new HashMap<>();
	
	//public Tree nodeTree = new Tree<NetObject>();
	
	public World() {
		
	}
	
	public void step(double dt) {
		flush();
		
		Iterator<NetObject> it = netObjects.iterator();
		while(it.hasNext()) {
			NetObject netObject = it.next();
			if (netObject.removed == true) {
				it.remove();
			}
			else {
				netObject.step(this, dt);
				
			}
		}
		
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
			clientNetObjects.remove(clientId);
		}
		else
			System.out.println("ruh roh");
	}
	
	private void flush() {		
		// merge new objects into main list
		Iterator<NetObject> it1 = netObjectsQueue.iterator();
		while (it1.hasNext()) {
			NetObject netObject = it1.next();
			netObjects.add(netObject);
			System.out.println("... added new netobject:\n\tid " + netObject.getId() + 
					"\n\tclientId " + netObject.getClientId() );
			
		}
		netObjectsQueue.clear();
		
		Iterator<NetObject> it2 = netObjects.iterator();
		while (it2.hasNext()) {
			NetObject netObject = it2.next();
			if (netObject.isRemoved()) {
				System.out.println("... found and removed a net object:\n\tid " + netObject.getId() + 
						"\n\tclientId " + netObject.getClientId() );
				it2.remove();
			}			
		}
		
		//System.out.println("... cleanups:\n\tnetobject size: " + netObjects.size() + 
		//		"\n\tclient netobjects size: " + clientNetObjects.size());
	}
}
