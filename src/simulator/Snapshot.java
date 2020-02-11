package simulator;

import main.Config;
import simulator.netobjects.NetObject;
import simulator.netobjects.Player;
import simulator.netobjects.SentientCube;

public class Snapshot {
	
	private NetObject[] snapshot = new NetObject[Config.SnapshotLimit];
	
	public Snapshot() {
		for(int i = 0; i < Config.SnapshotLimit; i++) { 
			snapshot[i] = null;
		}
	}
	

	public NetObject last() {
		return snapshot[Config.SnapshotLimit - 1];
	}
	
	/* shifts the array left */
	public void push(NetObject object, double dt) {
		NetObject[] snapshot2 = new NetObject[Config.SnapshotLimit];
		
		// shift snapshot array to the left
		for(int i = 1; i < Config.SnapshotLimit; i++) {
			snapshot2[i - 1] = snapshot[i];
		}
		snapshot = snapshot2;
		
		// create a copy of the netobject
		NetObject copy;
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
		}
		copy.snapTime = dt;
		snapshot[Config.SnapshotLimit - 1] = copy;
		
	}

}
