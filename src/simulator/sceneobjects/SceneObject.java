package simulator.sceneobjects;

import server.blobs.ChatBlob;
import server.blobs.DefaultBlob;
import server.blobs.JoinBlob;
import server.blobs.StateBlob;
import shared.Bitfield;
import shared.BoundingBox;
import shared.InputState;
import simulator.Snapshot;
import simulator.World;
import simulator.WorldLoader.WorldLoaderObject.WorldSceneObject.WorldSceneObjectArgs;

public abstract class SceneObject {
	public static class Types {
		public static final int Default = 0; // unknown / default type
		public static final int Player = 1;
		public static final int Tile = 2;
	}
	@SuppressWarnings("rawtypes")
	final static public Class[] types = {
		SceneObject.class, // 0 // should never be 0
		ScenePlayer.class,  // 1
		SceneTile.class, // 2
	};
	public SceneObject parent = null;
	// synchronized network object state
	
	public double[] moveDirection = {0.0f, 0.0f, 0.0f};
	public double[] speed = {0.0f, 0.0f, 0.0f};
	//public double[] bounds = {2.0f, 2.0f, 2.0f};
	//public double[] position = {50.0f, 50.0f, 0.0f};
	public double x = 0.0;
	public double y = 0.0;
	public double z = 0.0;
	//public ObjectBoundingBox boundingBox = new ObjectBoundingBox();
	
	//public double[] angles = {0.0f, 0.0f, 0.0f};
	public double yaw = 0.0;
	public double pitch = 0.0;
	public double roll = 0.0;
	public double yawAccumulator = 0.0;
	public double pitchAccumulator = 0.0;
	public double rollAccumulator = 0.0;
	public BoundingBox bb = new BoundingBox();
	
	public int clientId = -1;
	public long id = 0; // world id, set when added to the simulation
	public boolean removed = false; 
	
	public int type = SceneObject.Types.Default; // object type
	
	public Bitfield inputState = new Bitfield();
	public Snapshot snapshots = new Snapshot();
	
	public double snapTime = 0.0f; // set when a snapshot is taken and not used in state management
	public boolean stateChange = false; // signals to node that the object needs to be updated over network
	public boolean dynamic = true; // flag tells world if this object never moves or is sync'd over network
	
	
	public static SceneObject copy(SceneObject object) {
		//NetObject copy;
		int type = object.getType();
		if (type == SceneObject.Types.Player) {
			return new ScenePlayer(object);
		}
		else if (type == SceneObject.Types.Tile) {
			return new SceneTile(object);
		}
		else { 
			System.err.println("tried to copy a default type");
			return null;
		}
	}
	/* integrate state changes like input or actions 
	 * only called by world */
	public void integrate(InputState state, double dt) {
		//inputState
	};

	/* */
	
	public SceneObject() {
	}
	
	/* probably only going to be used by the map loader */
	public SceneObject(WorldSceneObjectArgs args, int type) {
		
		this.x = args.x;
		this.y = args.y;
		this.z = args.z;
		
		this.yaw = args.yaw;
		this.pitch = args.pitch;
		this.roll = args.roll;
		System.out.println(args.xscale + ", " + args.yscale);
		this.bb = new BoundingBox(this.x, this.y, this.z, args.xscale, args.yscale, args.zscale);
		

		
		this.type = type;
	}
	
	// this method is used for copying/cloning
	public SceneObject(SceneObject obj) {
		
		// copy old object to this one 
		if (obj == null) return;
		
		this.x = obj.x;
		this.y = obj.y;
		this.z = obj.z;
		
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
		this.snapshots = new Snapshot();
		this.bb = new BoundingBox(this.x, this.y, this.z, obj.bb.xscale, obj.bb.yscale, obj.bb.zscale);
		
	}
	
	public void setRemoved(boolean value) { this.removed = value; };
	public boolean isRemoved() { return this.removed; };
	
	public void setId(long value) { this.id = value; };
	public long getId() { return this.id; };
	
	public int getType() { return this.type; };
	
	public void setClientId(int v) { this.clientId = v; System.out.println(clientId);};
	public int getClientId() { return this.clientId; };
	
	
	
	abstract public void step(World world, double dt);

}
