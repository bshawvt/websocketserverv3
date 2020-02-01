package simulator.netobjects;

import java.util.Iterator;

import Models.CharacterModel;
import simulator.World;

public class Player extends NetObject {
	
	public CharacterModel model;
	public int clientId;
	
	public boolean removed = false; 
	
	
	public Player() {
		this.model = new CharacterModel();
	}
	public Player(CharacterModel model) {
		this.model = new CharacterModel(model);
	}
	
	void step(float dt) {
		//super.step(dt);
	}
	
	@Override
	public void step(World world, double dt) {
		// TODO Auto-generated method stub
		
	}

	
	
}
