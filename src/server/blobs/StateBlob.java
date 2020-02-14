package server.blobs;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import Dtos.StateChangeDto;
import simulator.netobjects.NetObject;

public class StateBlob extends MessageBlob {
	@SerializedName("objectType")
	public long objectType;
	public long getObjectType() { return this.objectType; };
	
	@SerializedName("input")
	public long input;
	public long getInputState() { return this.input; };
	
	@SerializedName("davids")
	public double[] angles = {0.0f, 0.0f, 0.0f};
	public double[] getAngles() { return this.angles; }
	
	
	/* net object's id from simulator */
	@SerializedName("id")
	public long id;
	public long getId() { return this.id; };
	
	/* approximate time the user generated this frame */
	@SerializedName("frame")
	public long frame;
	public long getFrame() { return this.frame; }
	
	/* designates that this netobject belongs to the user it is sent to */
	@SerializedName("me")
	public boolean me;
	public boolean getMe() { return this.me; }
	
	@SerializedName("position")
	public double[] position = {0.0f, 0.0f, 0.0f};
	public double[] getPosition() { return this.position; }
	
	@SerializedName("speed")
	public double[] speed = {0.0f, 0.0f, 0.0f};
	
	@SerializedName("removed")
	public boolean removed = false;
	
	
	public StateBlob() {
		
		
	}
	public StateBlob(StateChangeDto dto) {
		//super();
		this.objectType = dto.who.getType();
		this.type = MessageBlob.Type.StateBlob;
		this.frame = dto.frame;
		this.id = dto.who.getId();
		this.input = dto.who.inputState.get();
		 
		this.angles[0] = dto.who.angles[0];
		this.angles[1] = dto.who.angles[1];
		this.angles[2] = dto.who.angles[2];
		
	
		this.speed = dto.who.speed;
		
		this.removed = dto.who.removed;
		
		this.position[0] = dto.who.position[0];
		this.position[1] = dto.who.position[1];
		this.position[2] = dto.who.position[2];
		
	}
	
}
