package simulator.sceneobjects;


import Models.CharacterModel;
import shared.BadMath;
import shared.InputController;
import simulator.World;

public class ScenePlayer extends SceneObject {
	
	public CharacterModel model;
	
	public double strafe = 0.0;
	public boolean isJumping = false;
	public double lastJumpTime = 0.0;
	public boolean canJump = true;
	public ScenePlayer(SceneObject obj) {
		super(obj);
		type = SceneObject.Types.Player;
	}
	public ScenePlayer(CharacterModel model) {
		//super(null);
		this.model = new CharacterModel(model);
		type = SceneObject.Types.Player;
		x = model.getX();
		y = model.getY();
		z = model.getZ();
		z = 1.01;
	}
	
	@Override
	public void step(World world, double dt) {
		//System.out.println(this.inputState.get());
		System.out.println(this.yaw);
		if (this.inputState.compare(InputController.MAP_FIRE.bit) != 0) {
			//console.log("fire");
		}
		else if (this.inputState.compare(InputController.MAP_ALTFIRE.bit) != 0) {
			//console.log("alt fire");
		}
		else {

		}
		// action
		if (this.inputState.compare(InputController.MAP_ACTION.bit) != 0) {
			//console.log("action");
		}
		else {
			
		}

		// jump
		if (this.inputState.compare(InputController.MAP_JUMP.bit) != 0) {

		}
		else {

		}		

		// forward and reverse
		if (this.inputState.compare(InputController.MAP_FORWARD.bit) != 0) {
			this.speed[0] = -0.1;
		}
		else if (this.inputState.compare(InputController.MAP_BACKWARD.bit) != 0) {
			this.speed[0] = 0.1;
		}
		else {
			this.speed[0] = 0.0;
		}
		
		// strafe
		if (this.inputState.compare(InputController.MAP_LEFT.bit) != 0) {
			this.speed[1] = 0.1;
		}
		else if (this.inputState.compare(InputController.MAP_RIGHT.bit) != 0) {
			this.speed[1] = -0.1;
		}
		else {
			this.speed[1] = 0.0;
		}
		
		double nx = Math.sin(-this.yaw) * this.speed[0];
		double ny = Math.cos(-this.yaw) * this.speed[0];
		double nz = 0.0;
		
		if (this.speed[1] != 0.0)  {
			nx += Math.sin(-this.yaw + BadMath.D90TR) * this.speed[1];
			ny += Math.cos(-this.yaw + BadMath.D90TR) * this.speed[1];
		}
		
		this.x += nx;// + (0.71/2);
		this.y += ny;// + (0.71/2);
		this.z += nz;// + (0.5);
		System.out.println(x);
		
		//this.x = 0.0;
		//this.y = 0.0;
		//this.z = 10.0;

		/*if (this.prevInputState.get() != this.inputState.get()) {

		}*/


		
	}
	
	private void checkCollision(World world) {
		/*HashSet<SceneObject> set = world.tree.get(new BoundingBox(position, bounds, 25));
		for(SceneObject item : set) {
			System.out.println("there is an object of interest nearby.. " + item.id);
		}*/
		
	}

	
	
}
