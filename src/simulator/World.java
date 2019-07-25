package simulator;

import java.util.ArrayList;

import sql.SqlWriter;
import thread.SimulatorMessage;
import thread.ThreadMessage;
import util.L;

public class World {
	
	public static final double TIME_STEP = 1000/30;
	public static final double GRAVITY = -0.005;
	
	public long worldStarTime = System.nanoTime()/1000000;
	public long worldTime = worldStarTime;
	public long dt = 0; 
	
	private long currentStepTime = worldStarTime;
	private long elapsedStepTime = worldStarTime;
	
	private NetObjectManager netObjects = null;
	private SimulatorThread simulator = null;
	
	public World(SimulatorThread st) {
		L.d("World: Hello!");
		simulator = st;
		netObjects = new NetObjectManager(this);
	}
	public SimulatorThread getSimulator() { return simulator; };
	public NetObjectManager getNetObjectManager() { return netObjects; }
		
	
	private ArrayList<NetObjectSnapshot> frameSnapshots = new ArrayList<NetObjectSnapshot>();
	private ArrayList<NetObjectSnapshot> takeWorldFrameSnapshot(long dt) {
		ArrayList<NetObject> list = netObjects.getNetObjects();
		for(int i = 0; i < list.size(); i++) {
			NetObjectSnapshot snap = list.get(i).getLastSnapshot();
			if (snap != null)
				if (snap.isChanged() == true) 
					frameSnapshots.add(list.get(i).getLastSnapshot());
			
		}
		
		return frameSnapshots;
	}

	public void simulate() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		currentStepTime = System.nanoTime()/1000000; // dt in milliseconds
		worldTime = System.nanoTime()/1000000;; 
		
		
		if (currentStepTime > elapsedStepTime + 1000) {
			elapsedStepTime = currentStepTime;
			L.e("skipped frame");
		}
		
		getNetObjectManager().flushQueue();
		while(elapsedStepTime < currentStepTime) { // oddwarg loop!
			
			elapsedStepTime+=World.TIME_STEP;
			netObjects.integrate(worldTime);
			simulator.dispatchServerThreadMessage(new ThreadMessage(ThreadMessage.FROM_SIMULATOR, 
					ThreadMessage.TYPE_SET,
					new SimulatorMessage(new WorldFrame(takeWorldFrameSnapshot(worldTime), worldTime)) ));
			clearFrameSnapshots();
		}
	}
	private void clearFrameSnapshots() {
		frameSnapshots.clear();
		
	}
	private void flushQueues() {

	}
}
