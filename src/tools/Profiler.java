package tools;

public class Profiler {
	
	private long startTime;
	private long stopTime;
	
	public Profiler() {
		this.startTime = 0;
		this.stopTime = 0;
	}
	
	public void start() {
		this.startTime = System.nanoTime()/1000000;
	}
	public void stop() {
		
	}
}
