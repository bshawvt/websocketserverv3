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

	public void step(World world, double dt, int s) {
		
		Iterator<NetObject> it = world.netObjects.iterator();
		while(it.hasNext()) {
			NetObject netObject = it.next();
			count++;
		}
		count = 0;
		
		
		/*
		// simulate a large search
		while(tmpCount < s) {
			while(tmpCount2 < s) {
				while(tmpCount3 < s) {
					while(tmpCount4 < s) {
						count++;
						tmpCount4++;
					}
					count++;
					tmpCount3++;
				}
				count++;
				tmpCount2++;
			}
			count++;
			tmpCount++;
		}
		//System.out.println("total iterations per step: " + count);
		count = 0;
		tmpCount = 0;
		tmpCount2 = 0;
		tmpCount3 = 0;
		tmpCount4 = 0;*/
	}
}
