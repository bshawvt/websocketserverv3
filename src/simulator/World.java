package simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Models.CharacterModel;
import server.Client;

public class World {
	
	private ArrayList<NetObject> netObjects;
	private ArrayList<NetObject> netObjectsQueue;
	private HashMap<Integer, NetObject> clientNetObjects;
	public World() {
		
		this.clientNetObjects = new HashMap<>();
		this.netObjects = new ArrayList<>();
		this.netObjectsQueue = new ArrayList<>();
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
				netObject.step(dt);
			}
		}
		
		//System.out.println( "id:" + id + ", dt: " + dt);
		
	}
	
	public void addNetObject(int clientId, CharacterModel model) {
		
		if (model == null) return;
		NetObject obj = new NetObject(model);
		obj.clientId = clientId;
		this.netObjectsQueue.add(new NetObject(model));
			
	}
	public void removeNetObject(int clientId) {
		
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
