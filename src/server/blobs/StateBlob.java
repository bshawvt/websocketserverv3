package server.blobs;

import com.google.gson.annotations.SerializedName;

public class StateBlob extends MessageBlob {
	@SerializedName("input")
	private long input;
	public long getInputState() { return this.input; };
	
	@SerializedName("yaw")
	private double yaw;
	public double getYaw() { return this.yaw; }
	
	@SerializedName("pitch")
	private double pitch;
	public double getPitch() { return this.pitch; }
	
	@SerializedName("roll")
	private double roll;
	public double getRoll() { return this.roll; }
	
	/**
	 * approximate time the user generated this frame 
	 */
	@SerializedName("frameTime")
	private long frameTime;
	public long getFrameTime() { return this.frameTime; }
	
}
