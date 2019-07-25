package simulator;

import java.util.ArrayList;
import java.util.Iterator;

import dtos.CharacterDto;
import server.ServerThread;
import simulator.entities.EntityObject;
import sql.SqlWriter;
import thread.SimulatorMessage;
import thread.ThreadMessage;
import util.L;

public class NetObjectManager {
	
	// 7262018 refactor
	public void addNetObject(EntityObject obj, long owner, long clientId) {
		NetObject tObj = null;
		
		tObj = new NetObject(this, obj, getNextNetObjectId());
		tObj.setCharacterDto(new CharacterDto(), owner, clientId);
		tObj.getCharacterDto().setClientId(clientId);
		tObj.getCharacterDto().setOwner(owner);
		
		// if clientId is not 0 then this must be a clients netobject
		if (clientId > 0) {
			clientNetObjectsQueue.add(tObj);
		}
		netObjectsQueue.add(tObj);
		return;
	}
	// end
	
	private ArrayList<NetObject> netObjectsQueue = null;
	private ArrayList<NetObject> netObjects = null;
	
	private ArrayList<NetObject> clientNetObjectsQueue = null;
	private ArrayList<NetObject> clientNetObjects = null;
	
	
	
	private long nextNetObjectId = 0;
	
	private final World world;
	
	public NetObjectManager(World world) {
		netObjects = new ArrayList<NetObject>();
		netObjectsQueue = new ArrayList<NetObject>();

		clientNetObjects = new ArrayList<NetObject>();
		clientNetObjectsQueue = new ArrayList<NetObject>();

		this.world = world;
	}
	public World getWorld() {
		return world;
	}
	public synchronized long getNextNetObjectId() {
		return ++nextNetObjectId;
	}
	public synchronized NetObject getNetObjectByClientId(long clientIid) {
		for(int i = 0; i < clientNetObjects.size(); i++) {
			NetObject _obj = clientNetObjects.get(i);
			if (_obj.getCharacterDto().getClientId() == clientIid) {
				return _obj;
			}
		}
		return null;
	}
	public synchronized NetObject getNetObjectById(long id) {
		for(int i = 0; i < netObjects.size(); i++) {
			if (netObjects.get(i).getId() == id) {
				return netObjects.get(i);
			}
		}
		return null;
	}
	public synchronized NetObject getNetObjectByOwner(long id) {
		for(int i = 0; i < netObjects.size(); i++) {
			CharacterDto tmpDto = netObjects.get(i).getCharacterDto();
			if (tmpDto != null) {
				if (tmpDto.getOwner() == id) {
					return netObjects.get(i);
				}
			}
		}
		return null;
	}
	
	
	public synchronized void removeNetObject(NetObject obj) {
		obj.setRemoved(true);
		netObjectsQueue.add(obj);
		if (obj.getCharacterDto().getClientId() > 0) {
			clientNetObjectsQueue.add(obj);
		}
	}
	public synchronized void removeNetObjectByClientId(long clientId) {
		NetObject obj = getNetObjectByClientId(clientId);
		if(obj==null) return;
		obj.setRemoved(true); 
		netObjectsQueue.add(obj);
	}
	public synchronized void _removeNetObject(long id) {
		L.d("Size of netObjects: " + netObjects.size());
		if (netObjects.size() <= 0) { return; };
		
		for(int i = 0; i < netObjects.size(); i++) {
			if (netObjects.get(i).getId() == id) {
				if (netObjects.get(i).getCharacterDto().getClientId() > 0) {
					if (clientNetObjects.size() <= 0) { continue; }
					for(int x = 0; x < clientNetObjects.size(); x++) {
						L.d("clientNetObject size: " + clientNetObjects.size() + ", index: " + x);
						if (clientNetObjects.get(x).getCharacterDto().getClientId() == netObjects.get(i).getCharacterDto().getClientId()) {
							clientNetObjects.remove(x);
							break;
						}
					}
				}
				getWorld().getSimulator().dispatchServerThreadMessage(new ThreadMessage(ThreadMessage.FROM_SIMULATOR, 
						ThreadMessage.TYPE_REMOVE, 
						new SimulatorMessage( netObjects.get(i).getCharacterDto() )));//netObjects.get(i)));
				netObjects.remove(i);
				L.d("NetObjectManager: removeNetObject: removed a netobject from simulation");
				break;
				//return;
			}
		}
		return;
	}
	
	public synchronized void flushQueue() {
		for(int i = 0; i < netObjectsQueue.size(); i++) {
			if (netObjectsQueue.get(i).isRemoved() == true) {
				_removeNetObject(netObjectsQueue.get(i).getId());
				continue;
			}
			netObjects.add(netObjectsQueue.get(i));
			if (netObjectsQueue.get(i).getCharacterDto().getClientId() > 0) {
				clientNetObjects.add(netObjectsQueue.get(i));
			}
			L.d("NetObjectManager: flushQueue: added new netObject to simulation..");
			getWorld().getSimulator().dispatchServerThreadMessage(new ThreadMessage(ThreadMessage.FROM_SIMULATOR, 
					ThreadMessage.TYPE_CREATE, 
					new SimulatorMessage(netObjects.get(i).getCharacterDto()) ));
		}
		netObjectsQueue.clear();
	}
	public ArrayList<NetObject> getNetObjects() {
			
			return netObjects; 
	}
	public void integrate(long dt) {
		for(int i = 0; i < netObjects.size(); i++) {
			NetObject obj = netObjects.get(i);
			if (obj.getCharacterDto().getClientId() == 1) {
				//obj.getBase().setForwardVelocity(5);
			}
			if (obj.getReadyState() == true) {
				obj.step(dt);
				/*if (obj.getLastGameState1() != obj.getBase().getGameState1().getMask() || 
						obj.getLastState() != obj.getBase().getInputState().getMask() || 
							obj.getLastForwardAngle() != obj.getBase().getForwardAngle() || 
								obj.getValidity() == false) {
					getWorld().getSimulator().dispatchServerThreadMessage(new ThreadMessage(ThreadMessage.FROM_SIMULATOR, ThreadMessage.TYPE_SET, netObjects.get(i)));
					obj.setValidity(true);
					obj.setLastState(obj.getBase().getInputState().getMask());
					obj.setLastGameState1(obj.getBase().getGameState1().getMask());
					obj.setLastForwardAngle(obj.getBase().getForwardAngle());
				} else {
					if (obj.getLastState() == 0) {
						
					}
				}*/
			}
		}
	}
}
