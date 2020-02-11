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
	}
	
	@Override
	public void step(World world, double dt) {
		// TODO Auto-generated method stub
		//health+=1;
		
	}

	
	
}
