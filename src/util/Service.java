package util;

public class Service {
	private long lastUpdate = 0;
	private long interval = 0; 
	private String service = null;
	
	public Service(long interval, String svc) {
		this.interval = interval;
		service = svc;
	}

	public void setLastUpdate(long t) { lastUpdate = t; }
	public long getLastUpdate() { return lastUpdate; }
	public long getInterval() { return interval; }
	public Object getService() { return service; }
	
}