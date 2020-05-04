package server.blobs;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import Dtos.StateChangeDto;
import simulator.sceneobjects.SceneObject;

public class StateBlob extends MessageBlob {
	@SerializedName("objectType")
	public long objectType;
	public long getObjectType() { return this.objectType; };
	
	@SerializedName("input")
	public int input = -1;
	public long getInputState() { return this.input; };
	
	@SerializedName("angles")
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
		 
		this.angles[0] = dto.who.yaw;
		this.angles[1] = dto.who.pitch;
		this.angles[2] = dto.who.roll;
		
	
		this.speed[0] = dto.who.speed[0];
		this.speed[1] = dto.who.speed[1];
		this.speed[2] = dto.who.speed[2];
		
		this.removed = dto.who.removed;
		
		this.position[0] = dto.who.x;
		this.position[1] = dto.who.y;
		this.position[2] = dto.who.z;
		
	}
	
}
