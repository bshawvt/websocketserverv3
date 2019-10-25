package simulator;

import java.util.Iterator;

import Models.CharacterModel;

public class NetObject {
	
	public CharacterModel model;
	public int clientId;
	
	public boolean removed = false; 
	
	
	public NetObject() {
		this.model = new CharacterModel();
	}
	public NetObject(CharacterModel model) {
		this.model = new CharacterModel(model);
	}

	
	private int tmpCount = 0;
	private int tmpCount2 = 0;
	private int tmpCount3 = 0;
	private int tmpCount4 = 0;
	private int count = 0;
	
	private int hp = 1000;
	

	public void step(World world, double dt, int s) {
		
		// simulate a very active object
		for(int i = 0; i < s; i++) {
			//NetObject netObject = world.netObjects.get((int)Math.round(Math.random() * (world.netObjects.size() - 1)));
		//Iterator<NetObject> it = world.netObjects.iterator();
		//while(it.hasNext()) {
			//if (count > s) break;
			//else count++;
			//NetObject netObject = it.next();
			if (Math.floor(Math.random() * 8) == 1) {
				tmpCount4++;
			}
			else if (Math.floor(Math.random() * 8) == 2)
				tmpCount++;
			else if (Math.floor(Math.random() * 8) == 3)
				tmpCount2--;
			else if (Math.floor(Math.random() * 8) == 4)
				tmpCount3++;
			else if (Math.floor(Math.random() * 8) == 5)
				tmpCount3--;
			else if (Math.floor(Math.random() * 8) == 6)
				tmpCount3 += -200 + Math.floor(Math.random() * 200);
			
			//netObject.smack(this, tmpCount3);
		}	
		count = 0;
	}
	
	public void smack(NetObject smacker, int t) {
		hp -= t;
	}
}
