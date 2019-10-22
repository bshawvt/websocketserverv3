package simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Models.CharacterModel;
import server.Client;
import tools.Metrics;

public class World {
	
	public ArrayList<NetObject> netObjects = new ArrayList<>();
	public ArrayList<NetObject> netObjectsQueue = new ArrayList<>();
	
	//public Metrics metrics = new Metrics();
	//private HashMap<Integer, NetObject> clientNetObjects;
	
	
	
	public World() {
		
	}
	
	public void step(double dt, int s) {
		flush();
		
		Iterator<NetObject> it = netObjects.iterator();
		while(it.hasNext()) {
			NetObject netObject = it.next();
			if (netObject.removed == true) {
				it.remove();
			}
			else {
				netObject.step(this, dt, s);
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
		NetObject obj = new NetObject(model);
		obj.clientId = clientId;
		this.netObjectsQueue.add(new NetObject(model));
			
	}
	/**
	 * removes the clients character from the simulation
	 * @param clientId
	 */
	public void removeNetObject(int clientId) {
		
	}
	
	/**
	 * add a server owned character to the simulation
	 */
	public void addNetObject() {
		this.netObjectsQueue.add(new NetObject());
	}
	public void removeNetObject() {
		
	}
	
	private void flush() {		
		// merge new objects into main list
		Iterator<NetObject> it = netObjectsQueue.iterator();
		while (it.hasNext()) {
			NetObject netObject = it.next();
			netObjects.add(netObject);
			
		}
		netObjectsQueue.clear();
	}
}
