package Dtos;

import java.util.ArrayList;
import java.util.Iterator;

import server.blobs.StateBlob;
import simulator.netobjects.NetObject;

public class StateChangeDto {
	
	public final NetObject who;
	public final ArrayList<NetObject> to;
	public final long frame;
	
	public int clientId = -1;
	public int inputState;
	public double[] angles = {0.0f, 0.0f, 0.0f};
	
	public StateChangeDto() {
		this.to = null;
		this.who = null;
		this.frame = 0;
		this.angles = null;
	}
	public StateChangeDto(int clientId, StateBlob blob ) {
		this.clientId = clientId;
		this.who = null;
		this.to = null;
		this.frame = 0;
		
		if (blob.input != -1)
			this.inputState = blob.input;
		if (blob.angles != null) {
			this.angles[0] = blob.angles[0];
			this.angles[1] = blob.angles[1];
			this.angles[2] = blob.angles[2];
		}
		
		
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
