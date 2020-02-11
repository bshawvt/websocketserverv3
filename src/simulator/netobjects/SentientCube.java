package simulator.netobjects;

import simulator.World;

public class SentientCube extends NetObject {
	

	public int type = NetObject.Types.SentientCube;
	
	public SentientCube(NetObject obj) {
		super(obj);
		type = NetObject.Types.SentientCube;
	}
	
	@Override
	public void step(World world, double dt) {
		// TODO Auto-generated method stub
		
	}
}