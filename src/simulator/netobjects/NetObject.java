package simulator.netobjects;

import Models.CharacterModel;
import simulator.InputState;
import simulator.World;
import threads.Bitmask;

public abstract class NetObject {
	// synchronized network object state
	public double[] moveDirection = {0.0f, 0.0f, 0.0f};
	public double[] speed = {0.0f, 0.0f, 0.0f};
	
	public double yaw = 0.0f;
	public double pitch = 0.0f;
	public double roll = 0.0f;
	
	public long id = 0;
	public boolean removed = false;
	
	public static InputState state;
	
	
	
	/* integrate state changes like input or actions 
	 * only called by world */
	public void integrate(InputState state) {
		
	};
	
	public void setRemoved(boolean value) {
		this.removed = value;
	};
	public void setId(long value) {
		this.id = value;
	};
	
	public boolean isRemoved() { return this.removed; };
	public long getId() { return this.id; };
	
	
	abstract public void step(World world, double dt);

}
