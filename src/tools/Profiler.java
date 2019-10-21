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
	public void start(String name, int expires) {
		Profile profile = profiles.get(name);
		if (profile == null) {
			profile = new Profile();
			profiles.put(name, profile);
		}
		profile.start();
		//profile.expires = profile.now() + expires;
	}
	public void stop(String name) {
		Profile profile = profiles.get(name);
		if (profile == null) {
			profile = new Profile();
			profiles.put(name, profile);
		}
		profile.stop();
	}
	public boolean hasElapsed(String name, long expires) {
		Profile profile = profiles.get(name);
		if (profile == null) {
			profile = new Profile();
			profiles.put(name, profile);
			profile.start();
			profile.expires = profile.now() + expires;
			
		}
		if (profile.hasElapsed()) {
			//profiles.remove(profile);
			profile.expires = profile.now() + expires;
			return true;
		}
		return false;
	}
	
	
	private class Profile {
		private long startTime;
		private long stopTime;
		private long elapsed;
		//private HashMap<String, Long> iterator;
		private long iterations;
		private long expires;
		
		private long average;
		static private final int MaxSteps = 10;
		private long[] averages = new long[MaxSteps]; // for average
		private long step;

		
		public void start() {
			this.startTime = now();
		}
		public void stop() {
			this.stopTime = now();
			this.elapsed = stopTime - startTime;
		}
		
		public boolean hasElapsed() {
			if (now() > this.expires) {
				return true;
			}
			return false;
		}
		
		public long now() {
			return System.nanoTime()/1000000;
		}
			
	}
	
}
