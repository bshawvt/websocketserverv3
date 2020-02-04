package simulator.netobjects;

import java.util.Iterator;

import Models.CharacterModel;
import simulator.World;

public class Player extends NetObject {
	
	public CharacterModel model;
	public boolean removed = false; 
	
	
	public Player(CharacterModel model) {
		this.model = new CharacterModel(model);
	}
	
	@Override
	public void step(World world, double dt) {
		// TODO Auto-generated method stub
		
	}

	
	
}
