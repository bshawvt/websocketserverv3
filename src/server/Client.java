package server;

import java.util.ArrayList;

import org.java_websocket.WebSocket;

import dtos.CharacterDto;
import dtos.UserAccountDto;
import util.L;

public class Client {
	
	// refactor 7262018
	private CharacterDto characterDto;
	// end
	
	private WebSocket conn = null;
	private int state = State.NONE;
	private boolean isRemoved = false;
	private long connectionStartTime = 0;
	private long id = 0; // client id
	private ArrayList<Long> latency = null; // average latency
	private long pingTimeStart = 0; //
	private long pingTimeEnd = 0;
	private long lastPingTime = 0;
	private long nextPingId = 0;
	
	private long requestId = 0; // from client, last id
	
	
	private String unauthenticatedUsername = null;
	
	private String frames = null;
	private long frameSize = 0;
	
	public void setPingTimeStart(long v) { pingTimeStart = v; };
	public void setPingTimeEnd(long v) { pingTimeEnd = v; };
	public void setLastPongTime(long v) { lastPingTime = v; };

	public long getPingTimeStart() { return pingTimeStart; };
	public long getPingTimeEnd() { return pingTimeEnd; };
	public long getLastPongTime() { return lastPingTime; };
	
	private UserAccountDto userAccountDto;
	public void setUserAccountDto(UserAccountDto dto) { userAccountDto = dto; };
	public UserAccountDto getUserAccountDto() { return userAccountDto; };
	
	
	public Client(WebSocket conn, long id) {
		this.conn = conn;
		this.id = id;
		L.d("Client: constructor #" + id);
		setConnectionStartTime(System.nanoTime() / 1000000);
		unauthenticatedUsername = "";
		latency = new ArrayList<Long>();
		
		setLastPongTime(connectionStartTime);
	}
	public long getCurrentPingId() { return nextPingId; };
	public long getNextPingId() { return ++nextPingId; };
	public long immediateSend(String msg) {
		String message = "{ \"f\": { \"size\": " + 1 + ", \"request\":" + -1 + ", \"blobs\": [" + msg + "] } }";
		if (!conn.isClosed()) {
			conn.send(message);
		}
		return message.length();
	}
	public long send() {
		if (frames !=null) {
			String message = "{ \"f\": { \"size\": " + frameSize + ", \"request\":" + requestId + ", \"blobs\": [" + frames + "] } }";
			//L.d("sent off requestId="+requestId);
			if (!conn.isClosed()) {
				conn.send(message);
			}
			frames = null;
			frameSize = 0;
			//requestId=0; // invalidate any 'me' updates if they are not expected on the client
			return message.length();
		}
		return 0;
	}
	public long getRequestId() {
		//L.d("requestId for whoever="+requestId);
		return requestId;
	}
	public void setRequestId(long id) {
		requestId = id;
		//L.d("requestId for whoever is now="+requestId);
	}
	public void pushMessage(String msg) {
		if (frames == null) {
			frames = msg;
		}
		else {
			frames += ", " + msg;
		}
		frameSize++;
	}
	public void setUnauthenticatedUsername(String name) {
		unauthenticatedUsername = name;
	}
	public String getUnauthenticatedUsername() {
		return unauthenticatedUsername;
	}
	public boolean isRemoved() {
		return isRemoved;
	}
	public WebSocket getConnection() {
		return conn;
	}
	public void setRemoved(boolean flag) {
		isRemoved = flag;
	}
	public boolean isConnection(WebSocket conn) {
		if (!this.conn.equals(conn)) { return false; }
		return true;
	}
	public int getState() {
		//L.d("Client: state: " + state);
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getConnectionStartTime() {
		return connectionStartTime;
	}
	public long getId() {
		return id;
	}
	public long getAverageLatency() {
		//System.out.println(latency.size());
		long avg = 0;
		for(int i = 0; i < latency.size(); i++) {
			avg+=latency.get(i);
		}
		if (avg!=0  && latency.size()!=0)
			return avg/latency.size();
		return 0;
	};
	public long getLatency() { 
		
		return (latency.size() > 0 ? latency.get(latency.size() - 1) : 999);
	};
	public void setLatency(long v) { 
		if (latency.size() >= 25) {
			latency.clear();
		}
		latency.add(new Long(v));
	};
	
	public void setConnectionStartTime(long connectionStartTime) {
		this.connectionStartTime = connectionStartTime;
	}
	public class State {
		public static final int NONE = 0;
		public static final int READY = 1;
	}
	public CharacterDto getCharacterDto() {
		return characterDto;
	}
	public void setCharacterDto(CharacterDto dto) {
		this.characterDto = dto;
	}
	
}
