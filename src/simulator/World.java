package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import Models.CharacterModel;
import main.Config;
import simulator.netobjects.NetObject;
import simulator.netobjects.Player;

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
			if (netObject.isRemoved()) {
				System.out.println("... found and removed a net object:\n\tid " + netObject.getId() + 
						"\n\tclientId " + netObject.getClientId() );
				it.remove();
			}			
			else {
				
				netObject.step(this, dt);
				// only push a new snapshot after 100 ms have passed
				NetObject last = netObject.snapshots.last();// != null ? netObject.snapshots.last()._snapTime : 0;
				
				// compare with last state before pushing the next snapshot
				if (objectStateHasChanged(netObject, last)) {
					// this object is different than the last frame!
					System.err.println("the net objects state has changed");
					//NetObject[] cellContents = world.in
				}
				
				if (last == null || dt > last.snapTime + Config.SnapshotDelay)
					netObject.snapshots.push(netObject, dt);
			}
		}
		
	}
	public boolean objectStateHasChanged(NetObject a, NetObject b) {
		if (b == null) 
			return true;
		
		if ((a.inputState.get() != b.inputState.get()) ||
				(a.yaw != b.yaw) ||
				(a.pitch != b.pitch) ||
				(a.roll != b.roll) ||
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
			
		}
		netObjectsQueue.clear();
	}
}
