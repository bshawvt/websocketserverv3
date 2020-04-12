package simulator.netobjects;

import java.util.HashSet;
import java.util.Iterator;

import Models.CharacterModel;
import simulator.World;
import tools.ObjectBoundingBox;

public class Player extends NetObject {
	
	public CharacterModel model;
	
	
	public Player(NetObject obj) {
		super(obj);
		type = NetObject.Types.Player;
	}
	public Player(CharacterModel model) {
		//super(null);
		this.model = new CharacterModel(model);
		type = NetObject.Types.Player;
		position[0] = 100;//Math.random() * 10;
		position[1] = 100;//Math.random() * 10;
		position[2] = 5;//Math.random() * 8;
	}
	
	@Override
	public void step(World world, double dt) {
		
		checkCollision(world);
		
		// TODO Auto-generated method stub
		//health+=1;
		/*position[0] = Math.random() * 100;
		position[1] = Math.random() * 100;
		position[2] = Math.random() * 8;*/

		
		//System.out.println("?");
		
		//this.boundingBox.set(position, bounds);
		
		
	}
	
	private void checkCollision(World world) {
		HashSet<NetObject> set = world.tree.get(new ObjectBoundingBox(position, bounds, 25));
		
	}

	
	
}
