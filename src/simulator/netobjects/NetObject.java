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
	
	public boolean removed = false;
	
	public static InputState state;
	
	
	/* integrate state changes like input or actions 
	 * only called by world */
	void integrate(InputState state) {
		
	};
	
	abstract public void step(World world, double dt);

}
