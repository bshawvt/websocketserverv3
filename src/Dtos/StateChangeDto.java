package Dtos;

import java.util.ArrayList;
import java.util.Iterator;

import simulator.netobjects.NetObject;

public class StateChangeDto {
	
	public final NetObject who;
	public final ArrayList<NetObject> to;
	public final long frame;
	
	public StateChangeDto() {
		this.to = null;
		this.who = null;
		this.frame = 0;
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
	
}
