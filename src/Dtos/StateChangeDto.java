package Dtos;

import java.util.ArrayList;
import java.util.Iterator;

import simulator.netobjects.NetObject;

public class StateChangeDto {
	
	public final NetObject who;
	public final ArrayList<NetObject> to;
	public final long frame;
	
	public int clientId = -1;
	public int inputState;
	public double[] angles;
	
	public StateChangeDto() {
		this.to = null;
		this.who = null;
		this.frame = 0;
		this.angles = null;
	}
	public StateChangeDto(NetObject who, ArrayList<NetObject> to, long frame) {
		this.frame = frame;
		this.to = new ArrayList<>();
		Iterator<NetObject> it = to.iterator();
		while (it.hasNext()) {
			NetObject netObject = it.next();
			this.to.add(NetObject.copy(netObject));
		}
		this.who = NetObject.copy(who);
	}
	
	public int getClientId() {
		return this.clientId;
	}
	
}
