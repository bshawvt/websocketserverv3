package simulator.netobjects;

import java.util.Iterator;

import Models.CharacterModel;
import simulator.World;

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
		position[0] = -5 + Math.random() * 10;
		position[1] = -5 + Math.random() * 10;
		position[2] = -5 + Math.random() * 8;
	}
	
	@Override
	public void step(World world, double dt) {
		// TODO Auto-generated method stub
		//health+=1;
		
	}

	
	
}
