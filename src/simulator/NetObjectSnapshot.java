package simulator;

import simulator.entities.EntityObject;

public class NetObjectSnapshot {
	// todo: most of this should be a CharacterDTO
	private long timestamp = 0;
	private long clientId = 0;
	private long netObjId = 0;
	private double[] position = {0.0, 0.0, 0.0};
	private double forwardAngle = 0.0;
	private double radius = 0.0;
	private double health = 0.0;
	private int keyState = 0;
	private int gameState1 = 0;
	private boolean changed;
	private int stamina = 0;
	private double pitch = 0.0;
	public NetObjectSnapshot(EntityObject obj, long timestamp, long clientId, long netObjId) {
		
		this.timestamp = timestamp;
		this.clientId = clientId;
		this.netObjId  = netObjId;
		setChanged(false);
		
		setPosition(obj.getPosition());
		setForwardAngle(obj.getForwardAngle());
		setRadius(obj.getRadius());
		setHealth(obj.getHealth());
		setStamina(obj.getStamina());
		setKeyState(obj.getInputState().getMask());
		setGameState1(obj.getGameState1().getMask());
		setPitch(obj.getPitch());
		
	}
	public long getTimestamp() { return timestamp; };
	public long getClientId() { return clientId; }
	public long getNetObjectId() {
		// TODO Auto-generated method stub
		return netObjId;
	}
	public double getHealth() {
		return health;
	}
	public void setHealth(double health) {
		this.health = health;
	}
	public double[] getPosition() {
		return position;
	}
	public void setPosition(double[] position) {
		this.position = position;
	}
	public int getGameState1() {
		return gameState1;
	}
	public void setGameState1(int gameState1) {
		this.gameState1 = gameState1;
	}
	public int getKeyState() {
		return keyState;
	}
	public void setKeyState(int keyState) {
		this.keyState = keyState;
	}
	public double getForwardAngle() {
		return forwardAngle;
	}
	public void setForwardAngle(double forwardAngle) {
		this.forwardAngle = forwardAngle;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	public void setStamina(int v) {
		stamina = v;
	}
	public int getStamina() {
		return stamina;
	}
	private void setPitch(double pitch2) {
		pitch = pitch2;		
	}
	public double getPitch() {
		// TODO Auto-generated method stub
		return pitch;
	};
}
