package tools;

import java.util.HashMap;

public class Profiler {

	private HashMap<String, Profile> profiles;
	
	public Profiler() {
		this.profiles = new HashMap<>();
	}
	
	public void start(String name) {
		Profile profile = profiles.get(name);
		if (profile == null) {
			profile = new Profile();
			profiles.put(name, profile);
		}
		profile.start();
	}
	public void stop(String name) {
		Profile profile = profiles.get(name);
		if (profile != null) {
			profile.stop();
		}
	}
	
	private class Profile {
		private long startTime;
		private long stopTime;
		private long elapsed;
		
		private long average;
		static private final int MaxSteps = 10;
		private long[] averages = new long[MaxSteps]; // for average
		private int step;

		
		public void start() {
			this.startTime = System.nanoTime()/1000000;
		}
		public void stop() {
			this.stopTime = System.nanoTime()/1000000;
			averages[step%MaxSteps] = stopTime - startTime;
					
			step++;
			
			if (step%MaxSteps == 0) {
				average = 0;
				for(long a : averages) {
					average += a;
				}
				average = average / MaxSteps;						
			}
			
			this.elapsed = stopTime - startTime;
			//System.out.println(this.elapsed);
		}
	}
}
