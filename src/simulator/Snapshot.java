package simulator;

import main.Config;
import simulator.sceneobjects.SceneObject;
import simulator.sceneobjects.ScenePlayer;
import simulator.sceneobjects.SceneTile;

public class Snapshot {
	
	private SceneObject[] snapshot = new SceneObject[Config.SnapshotLimit];
	
	public Snapshot() {
		for(int i = 0; i < Config.SnapshotLimit; i++) { 
			snapshot[i] = null;
		}
	}
	

	public SceneObject last() {
		return snapshot[Config.SnapshotLimit - 1];
	}
	
	/* shifts the array left */
	public void push(SceneObject object, double dt) {
		SceneObject[] snapshot2 = new SceneObject[Config.SnapshotLimit];
		
		// shift snapshot array to the left
		for(int i = 1; i < Config.SnapshotLimit; i++) {
			snapshot2[i - 1] = snapshot[i];
		}
		snapshot = snapshot2;
		
		// create a copy of the netobject
		/*NetObject copy;
		int type = object.getType();
		
		if (type == NetObject.Types.Player) {
			copy = new Player(object);
		}
		else if (type == NetObject.Types.SentientCube) {
			copy = new SentientCube(object);
		}
		else { 
			System.err.println("tried to take a snapshot of a default type");
			return;
		}*/
		SceneObject copy = SceneObject.copy(object);
		copy.snapTime = dt;
		snapshot[Config.SnapshotLimit - 1] = copy;
		
	}

}
