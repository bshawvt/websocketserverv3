package simulator;

import Models.CharacterModel;

public class NetObject {
	
	public CharacterModel model;
	public int clientId;
	
	public boolean removed = false; 
	
	public NetObject() {
		
	}
	public NetObject(CharacterModel model) {
		this.model = new CharacterModel(model);
	}

	public void step(double dt) {
		
	}
}
