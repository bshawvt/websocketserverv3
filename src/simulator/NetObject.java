package simulator;

import java.util.ArrayList;

import dtos.CharacterDto;
import server.BlobFactory.UpdateBlobModel;
import simulator.entities.EntityObject;
import util.L;

public class NetObject {
	// dt not really dt
	private CharacterDto characterDto;
	public void setCharacterDto(CharacterDto dto, long owner, long clientId) {
		characterDto = new CharacterDto(dto); // client id is being set to -1
		getCharacterDto().setNetObjectId(getId());
		getCharacterDto().setOwner(owner);
		getCharacterDto().setClientId(clientId);
		//getCharacterDto().setClientId(dto.getClientId());
		
		
		double[] pos = {dto.getX(), dto.getY(), dto.getZ()};
		getBase().setPosition(pos);
		//if (getClientId() == -1)
		//	setClientId(getCharacterDto().getClientId());
	};
	public CharacterDto getCharacterDto() { return characterDto; };
	
	private NetObjectManager netObjectManager;
	public void setNetObjectManager(NetObjectManager mgr) { netObjectManager = mgr;	};
	public NetObjectManager getNetObjectManager() { return netObjectManager; };
	
	private long id = 0; // netobject id
	public void setId(long val) { id = val; }
	public long getId() { return id; }

	private boolean readyState = false;
	public void setReadyState(boolean v) { readyState = v; };
	public boolean getReadyState() { return readyState; };
	
	//private 
	/*ublic long accountId = -1;
	public void setAccountId(long v) { accountId = v; };
	public long getAccountId() { return accountId; };*/
	
	public long latency = 0;// delay
	public void setLatency(long v) { latency = v; };
	public long getLatency() { return latency; };
		
	private NetObjectSnapshot snapshots[] = new NetObjectSnapshot[10];
	public void addSnapshot(long dt) {
		//if (snapshots[snapshots.length-1].equals(obj))
		
		NetObjectSnapshot snap = new NetObjectSnapshot(getBase(), dt, getCharacterDto().getClientId(), id);
		snap.setChanged(hasStateChanged(snap));
		//System.out.println(snap.isChanged());
		if (snapshots.length > 9) {
			for(int i = 0; i < snapshots.length-1; i++)
			{
				snapshots[i] = snapshots[i+1];
			}
			snapshots[9] = snap;
		}
		else {
			snapshots[snapshots.length+1] = snap;
		}
		//L.d("added snapshot of netobject");
		setLastSnapshotTime(dt);
	}
	
	private long lastSnapshotTime = 0;
	public void setLastSnapshotTime(long dt) { lastSnapshotTime = dt; };
	public long getLastSnapshotTime() { return lastSnapshotTime; };
	
	private String name = null;
	public void setName(String name) { this.name = name; };
	public String getName() { return name; };
	
	private final EntityObject objectClass;
	public EntityObject getBase() { return objectClass; };
	private boolean isRemoved = false;
	public void setRemoved(boolean state) { isRemoved = true; };
	public boolean isRemoved() { return isRemoved; };
	
	private int lastState = 0;
	public void setLastState(int v) { lastState = v; };
	public int getLastState() { return lastState; };
	private int gameState1 = 0;
	public void setLastGameState1(int v) { gameState1 = v; };
	public int getLastGameState1() { return gameState1; };
	
	private double lastForwardAngle = 0.0;
	public void setLastForwardAngle(double v) { lastForwardAngle = v; };
	public double getLastForwardAngle() { return lastForwardAngle; };
	
	public void integrateUpdate(CharacterDto dto) { // push client state changes into the simulator
		getBase().setForwardAngle(dto.getAngle());
		getBase().setPitch(dto.getPitch());
		getBase().getInputState().setState(dto.getInputState());;
		//L.d("" + getBase().getPitch());
	}
	
	public NetObject(NetObjectManager mgr, EntityObject obj, long thisNetObjectId) {
		objectClass = obj;
		setId(thisNetObjectId);
		setNetObjectManager(mgr);		
			
		//L.d("NetObject: id " + id + ", clientId " + getCharacterDto().getClientId());
	}

	public void step(long dt) {
		getCharacterDto().setAngle(objectClass.getForwardAngle());
		getCharacterDto().setX(objectClass.getPosition()[0]);
		getCharacterDto().setY(objectClass.getPosition()[1]);
		getCharacterDto().setZ(objectClass.getPosition()[2]);
		getCharacterDto().setStamina(objectClass.getStamina());
		objectClass.step(dt);
		if (dt - getLastSnapshotTime() > 100)
			addSnapshot(dt);
	}
	public boolean hasStateChanged(NetObjectSnapshot snap) {
		NetObjectSnapshot l = snapshots[snapshots.length - 1];
		if (l!=null) {
			if (snap.getForwardAngle() != l.getForwardAngle() ||
					snap.getPitch() != l.getPitch() ||
					snap.getPosition() != l.getPosition() ||
					snap.getGameState1() != l.getGameState1() ||
					snap.getKeyState() != l.getKeyState() ||
					snap.getHealth() != l.getHealth() ) {
				return true;
			}
		}
		else {
			return true;
		}
		return false;
	}
	public NetObjectSnapshot getLastSnapshot() {

		return snapshots[snapshots.length-1];
	}
}
