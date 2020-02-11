package simulator.netobjects;

import simulator.Bitfield;
import simulator.InputState;
import simulator.Snapshot;
import simulator.World;

public abstract class NetObject {
	public static class Types {
		public static final int Default = 0; // unknown / default type
		public static final int Player = 1;
		public static final int SentientCube = 2;
	}
	public NetObject parent = null;
	// synchronized network object state
	public double[] moveDirection = {0.0f, 0.0f, 0.0f};
	public double[] speed = {0.0f, 0.0f, 0.0f};
	
	public double yaw = 0.0f;
	public double pitch = 0.0f;
	public double roll = 0.0f;
	
	public long clientId = -1;
	public long id = 0; // world id, set when added to the simulation
	public boolean removed = false; 
	
	public int type = NetObject.Types.Default; // object type
	
	public Bitfield inputState = new Bitfield();
	public Snapshot snapshots = new Snapshot();
	
	public double snapTime = 0.0f; // set when a snapshot is taken and not used in state management
	public boolean stateChange = false; // signals to node that the object needs to be updated over network

	
	/* integrate state changes like input or actions 
	 * only called by world */
	public void integrate(InputState state, double dt) {
		//inputState
	};

	/* */
	
	public NetObject() {
	}
	
	public NetObject(NetObject obj) {
		
		// copy old object to this one 
		if (obj == null) return;
		
		this.moveDirection[0] = obj.moveDirection[0];
		this.moveDirection[1] = obj.moveDirection[1];
		this.moveDirection[2] = obj.moveDirection[2];
		
		this.speed[0] = obj.speed[0];
		this.speed[1] = obj.speed[1];
		this.speed[2] = obj.speed[2];
		
		this.yaw = obj.yaw;
		this.pitch = obj.pitch;
		this.roll = obj.roll;
		
		this.clientId = obj.clientId;
		this.id = obj.id;
		this.removed = obj.removed; 
		
		this.type = obj.type;
				
		this.stateChange = obj.stateChange;
		
		this.inputState = new Bitfield(obj.inputState.get());
		this.snapshots = null;
		
	}
	
	public void setRemoved(boolean value) { this.removed = value; };
	public boolean isRemoved() { return this.removed; };
	
	public void setId(long value) { this.id = value; };
	public long getId() { return this.id; };
	
	public int getType() { return this.type; };
	
	public void setClientId(long v) { this.clientId = v; System.out.println(clientId);};
	public long getClientId() { return this.clientId; };
	
	
	
	abstract public void step(World world, double dt);

}
