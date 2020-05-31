package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import Dtos.StateChangeDto;
import Models.CharacterModel;
import main.Config;
import server.ServerThreadMessage;
import server.blobs.MessageBlob;
import shared.BoundingBox;
import shared.SOQuadTree;
import simulator.sceneobjects.SceneObject;
import simulator.sceneobjects.ScenePlayer;
import threads.Threads;
import ui.Form;

public class World {
	
	public ArrayList<SceneObject> sceneObjects = new ArrayList<>();
	public ArrayList<SceneObject> sceneObjectsQueue = new ArrayList<>();
	public ArrayList<SceneObject> staticSceneObjects = new ArrayList<>();
	public long uuidCounter = 0;
	
	//public Metrics metrics = new Metrics();
	private HashMap<Integer, SceneObject> clientSceneObjects = new HashMap<>();
	//public SOQuadTree tree = null;
	
	public long frameCount = 0; // increased every time a world step has completed
	
	public World() {
		new WorldLoader(this, Config.MapFilename);
	}
	
	public void step(double dt) {
		flush();
		
		//tree = new SOQuadTree(4, 0, 0, 500, 500, sceneObjects);
		
		Iterator<SceneObject> it = sceneObjects.iterator();
		while(it.hasNext()) {
			SceneObject sceneObject = it.next();

			if (sceneObject.isRemoved()) {
				System.out.println("... found and removed a sceneobject:\n\tid " + sceneObject.getId() + 
						"\n\tclientId " + sceneObject.getClientId() );
				it.remove();
			}

			sceneObject.step(this, dt);
			
			if (sceneObject.dynamic) {
				SceneObject last = sceneObject.snapshots.last();
				// compare with last state before pushing the next snapshot
				if (compareObjectStates(sceneObject, last)) {
					updateOverNetwork(sceneObject);
				}
				
				/* only push a new snapshot after 100 ms have passed 
				 * since they're limited to 10 per */
				if (last == null || dt > last.snapTime + Config.SnapshotDelay)
					sceneObject.snapshots.push(sceneObject, dt);
			}
			
			
		}
	
		frameCount++;		
		
	}
	public SceneObject getClientSceneObject(int clientId) {
		return clientSceneObjects.get(clientId);
	}
	/* prepares a frame for sending across the network for synchronizing 
	 * an object with any clients near by */
	public void updateOverNetwork(SceneObject who) {
		// list of objects
		//HashSet<SceneObject> receivers = world.tree.get(new BoundingBox(new double[] { x, y, z }, new double[] {bb.xscale, bb.yscale, bb.zscale}, 25));
		ArrayList<SceneObject> receivers = sceneObjects;//tree.getClients(who.position);
		StateChangeDto dto = new StateChangeDto(who, receivers, frameCount);
		Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Simulator, ServerThreadMessage.Type.Update, dto));
		//node.
		
	}
	/* returns true when a variable tied to state has changed */
	public boolean compareObjectStates(SceneObject a, SceneObject b) {
		if (b == null) // b is only null when the object has no previous snapshots
			return true;
		
		if ((a.inputState.get() != b.inputState.get()) ||
				(a.yaw != b.yaw) ||
				(a.pitch != b.pitch) ||
				(a.roll != b.roll) ||
				(a.removed != b.removed) ||
				(a.stateChange != b.stateChange)) {
			return true;
		}
		return false;
	}
	/* a server controlled scene object, likely only a map object */
	public void addSceneObject(SceneObject sceneObject) {
		sceneObjectsQueue.add(sceneObject);
	}
	/**
	 * adds a clients character to the simulation
	 * @param clientId
	 * @param model
	 */
	public void addSceneObject(int clientId, CharacterModel model) {
		
		if (model == null) return;
		ScenePlayer obj = new ScenePlayer(model);
		obj.setClientId(clientId);
		obj.setId(uuidCounter++);
		clientSceneObjects.put(clientId, obj);
		sceneObjectsQueue.add(obj);
			
	}
	/**
	 * removes the clients character from the simulation
	 * @param clientId
	 */
	public void removeSceneObject(int clientId) {
		SceneObject obj = clientSceneObjects.get(clientId);//.setRemoved(true);
		if (obj != null) {
			obj.setRemoved(true);
			//obj.removed = true;
			clientSceneObjects.remove(clientId);
		}
		else
			System.out.println("ruh roh");
	}
	
	/* merges new objects into state */
	private void flush() {		
		Iterator<SceneObject> it1 = sceneObjectsQueue.iterator();
		while (it1.hasNext()) {
			SceneObject obj = it1.next();
			sceneObjects.add(obj);
			System.out.println("... added new sceneobject:\n\tid " + obj.getId() + 
					"\n\tclientId " + obj.getClientId() );
			obj.stateChange = !obj.stateChange;
			
			/*final ArrayList<NetObject> tmpObjs = new ArrayList<>();
			netObjects.forEach((e) -> {
				tmpObjs.add(e);
			});
			Form.UpdateQuadPoints(tmpObjs);*/
			
		}
		sceneObjectsQueue.clear();
	}
}
