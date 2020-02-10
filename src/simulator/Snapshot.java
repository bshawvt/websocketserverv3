package simulator;

import main.Config;
import simulator.netobjects.NetObject;

public class Snapshot {
	
	private NetObject[] snapshot = new NetObject[Config.SnapshotLimit];
	
	public Snapshot() {
		/*for(int i = 0; i < Config.SnapshotLimit; i++) { 
			
		}*/
	}
	
	public NetObject last() {
		return snapshot[Config.SnapshotLimit];
	}
	
	public void push(NetObject object) {
		for(int i = 0; i < Config.SnapshotLimit; i++) { 
			
		}
		snapshot[Config.SnapshotLimit - 1];
	}

}
