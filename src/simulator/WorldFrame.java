package simulator;

import java.util.ArrayList;

public class WorldFrame {
	private long time = 0;
	private ArrayList<NetObjectSnapshot> snapshots = new ArrayList<NetObjectSnapshot>();
	public WorldFrame(ArrayList<NetObjectSnapshot> snaps, long time) {
		snapshots = (ArrayList<NetObjectSnapshot>) snaps.clone();
		this.time = time;
		
	}
	
	public ArrayList<NetObjectSnapshot> getSnapshots() {
		return snapshots;
	}
	public long getTimestamp() {
		return time;
	}
}
