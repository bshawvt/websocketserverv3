package simulator.entities;

import simulator.NetObjectSnapshot;
import simulator.World;
import util.Bitmask;

public abstract class EntityObject {
	
	private double[] position 	= 	{0.0, 0.0, 0.0};
	private double[] forwardSpeed	 	= 	{0.0, 0.0, 0.0};
	private double[] strafeSpeed	 	= 	{0.0, 0.0, 0.0};
	
	private double gravity = -0.005;
	public void setGravity(double v) { gravity = v; };
	public double getGravity() { return gravity; };

	public void setPosition(double[] position) { this.position = position; };
	public double[] getPosition() { return position; };
	public void setPositionX(double v) { position[0] = v;};
	public void setPositionY(double v) { position[1] = v;};
	public void setPositionZ(double v) { position[2] = v;};
	
	public void setForwardSpeed(double[] speed) { this.forwardSpeed = speed; };
	public double[] getForwardSpeed() { return forwardSpeed; };
	public void setForwardSpeedX(double v) { forwardSpeed[0] = v;};
	public void setForwardSpeedY(double v) { forwardSpeed[1] = v;};
	public void setForwardSpeedZ(double v) { forwardSpeed[2] = v;};
	
	public void setStrafeSpeed(double[] speed) { this.strafeSpeed = speed; };
	public double[] getStrafeSpeed() { return strafeSpeed; };
	public void setStrafeSpeedX(double v) { strafeSpeed[0] = v;};
	public void setStrafeSpeedY(double v) { strafeSpeed[1] = v;};
	public void setStrafeSpeedZ(double v) { strafeSpeed[2] = v;};
	
	
	private Bitmask inputState = null; // bitmask which contains information about keyboard state 
	public void setInputState(Bitmask state) { this.inputState = state; }
	public Bitmask getInputState() { return inputState; };
	
	private Bitmask gameState1 = null; // bitmask which contains information about keyboard state 
	public void setGameState1(Bitmask state) { this.gameState1 = state; }
	public Bitmask getGameState1() { return gameState1; };
	
	public void moveForward() {
		position[0] -= Math.sin(Math.PI/180 * forwardAngle) * getForwardSpeed()[0];
		position[1] += Math.cos(Math.PI/180 * forwardAngle) * getForwardSpeed()[1];
	};
	public void moveReverse() { 
		position[0] += Math.sin(Math.PI/180 * forwardAngle) * getForwardSpeed()[0];
		position[1] -= Math.cos(Math.PI/180 * forwardAngle) * getForwardSpeed()[1];
	};
	
	private double radius = 15;
	public void setRadius(int r) { radius = r; };
	public double getRadius() { return radius; };
	
	private double forwardAngle = 0.0;
	public void setForwardAngle(double rad) { forwardAngle = rad; };
	public double getForwardAngle() { return forwardAngle; };
	public void increaseForwardAngle() { forwardAngle+= getYawSpeed(); };// + ((dt/2)/1000); };
	public void decreaseForwardAngle() { forwardAngle-= getYawSpeed(); };// + ((dt/2)/1000); };
	
	private double yawSpeed = 5.00;
	public void setYawSpeed(double speed) { yawSpeed = speed; };
	public double getYawSpeed() { return yawSpeed; };
	
	private double health = 10;
	public void setHealth(double hp) { health = hp; };
	public double getHealth() { return health; };
	
	private int stamina = 100;
	public void setStamina(int v) { stamina = v; };
	public int getStamina() { return stamina; };
	public void increaseStamina(double v) { stamina += v; };
	public void decreaseStamina(double v) { stamina -= v; };
	
	public abstract void step(long dt);
	public EntityObject() {
		setInputState(new Bitmask());
		setGameState1(new Bitmask());
		position[0] = 0.0f;
		position[1] = 0.0f;
		position[2] = 0.0f;
	}
	private double pitch = 0.0;
	public double getPitch() {
		// TODO Auto-generated method stub
		return pitch;
	}
	public void setPitch(double p) {
		pitch = p;
	}
	
	

}
