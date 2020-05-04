package Dtos;

import java.util.ArrayList;
import java.util.Iterator;

import server.blobs.StateBlob;
import simulator.sceneobjects.SceneObject;

public class StateChangeDto {
	
	public final SceneObject who;
	public final ArrayList<SceneObject> to;
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
	public StateChangeDto(SceneObject who, ArrayList<SceneObject> to, long frame) {
		this.frame = frame;
		this.to = new ArrayList<>();
		Iterator<SceneObject> it = to.iterator();
		while (it.hasNext()) {
			SceneObject netObject = it.next();
			this.to.add(SceneObject.copy(netObject));
		}
		this.who = SceneObject.copy(who);
	}
	
	public int getClientId() {
		return this.clientId;
	}
	
}
