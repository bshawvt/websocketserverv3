package simulator;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import dtos.CharacterDto;
import server.BlobFactory.UpdateBlobModel;
import server.ServerThread;
import simulator.entities.Avatar;
import thread.DatabaseMessage;
import thread.SimulatorMessage;
import thread.ThreadMessage;
import util.L;

public class SimulatorThread implements Runnable {
	
	
	private ArrayList<World> worlds = null;
	private World world = null;
	//private Server server = null;
	public static final int VIRTUAL_VERSION = 0;
	private boolean running = false;

	private final LinkedBlockingQueue<ThreadMessage> _tMsgQueueSim;// me
	private final LinkedBlockingQueue<ThreadMessage> _tMsgQueueSrv;// = null;
	private final LinkedBlockingQueue<ThreadMessage> _tMsgQueueDb;
	
	public SimulatorThread(LinkedBlockingQueue<ThreadMessage> msgQueueSim, LinkedBlockingQueue<ThreadMessage> msgQueueSrv, LinkedBlockingQueue<ThreadMessage> msgQueueDb) {
		_tMsgQueueSrv = msgQueueSrv;
		_tMsgQueueSim = msgQueueSim;
		_tMsgQueueDb = msgQueueDb;
		
	}
	
	public synchronized void dispatchServerThreadMessage(ThreadMessage msg) {
		_tMsgQueueSrv.offer(msg);
	}
	public synchronized void dispatchDatabaseThreadMessage(ThreadMessage msg) {
		_tMsgQueueDb.offer(msg);
	}
	
	public synchronized void flushThreadMessages() {
		ThreadMessage _tMsg = null;
		while((_tMsg = _tMsgQueueSim.poll()) != null) {
			// todo: 
			if (_tMsg.getFrom() == ThreadMessage.FROM_SERVER) {
				if (_tMsg.getType() == ThreadMessage.TYPE_CREATE) {
					// add a new client netobject to simulation and initiate a character dto fetch for it
					
					// add object to world with only clientId and account owner Id
					world.getNetObjectManager().addNetObject(new Avatar(), _tMsg.getSimMessage().getCharacterDto().getOwner(), _tMsg.getSimMessage().getCharacterDto().getClientId()); 
					
					// request the character sheet
					ThreadMessage msg = new ThreadMessage(ThreadMessage.FROM_SIMULATOR, ThreadMessage.TYPE_GET, 
							new DatabaseMessage( new CharacterDto(_tMsg.getSimMessage().getCharacterDto()) )); 
					dispatchDatabaseThreadMessage(msg);
					
				} else if (_tMsg.getType() == ThreadMessage.TYPE_REMOVE) {
					// remove a client netobject from simulation and save their character data
					NetObject tmpNetObj = world.getNetObjectManager().getNetObjectByClientId(_tMsg.getSimMessage().getCharacterDto().getClientId());
					if (tmpNetObj == null) continue;
					
					// save character
					ThreadMessage msg = new ThreadMessage(ThreadMessage.FROM_SIMULATOR, ThreadMessage.TYPE_SET, new DatabaseMessage(tmpNetObj.getCharacterDto()));
					dispatchDatabaseThreadMessage(msg);
					
					// remove netobject
					world.getNetObjectManager().removeNetObjectByClientId(_tMsg.getSimMessage().getCharacterDto().getClientId());
					
				} else if (_tMsg.getType() == ThreadMessage.TYPE_GET) {
					
				} else if (_tMsg.getType() == ThreadMessage.TYPE_SET) {
					// client has updated their state
					//L.d("sim update");
					NetObject client = world.getNetObjectManager().getNetObjectByClientId(_tMsg.getSimMessage().getCharacterDto().getClientId());
					if (client==null) continue;
					client.integrateUpdate(_tMsg.getSimMessage().getCharacterDto());
		
				} else if (_tMsg.getType() == ThreadMessage.TYPE_NONE) {
					//xxx
				}
			}
			else if (_tMsg.getFrom() == ThreadMessage.FROM_DATABASE) {
							
				if (_tMsg.getType() == ThreadMessage.TYPE_GET) {
					L.d("SimulatorThread: flush: Database reply");
					long owner = _tMsg.getDbMessage().getCharacterDto().getOwner(); // user account id
					NetObject obj = world.getNetObjectManager().getNetObjectByOwner(owner);
					long  clientId = obj.getCharacterDto().getClientId();
					obj.setCharacterDto(_tMsg.getDbMessage().getCharacterDto(), owner, clientId);
					//L.d("stamina dto: "+_tMsg.getDbMessage().getCharacterDto().getMaxStamina());
					obj.setReadyState(true);
				}
				
			}
			else if (_tMsg.getFrom() == ThreadMessage.FROM_CONSOLE) {
				if (_tMsg.getType() == ThreadMessage.TYPE_COMMAND) {
					if (_tMsg.getData1().equals("crash")) {
						NetObject nobj = world.getNetObjectManager().getNetObjectById(-1);
						nobj.getCharacterDto().getClientId(); //  crash!
					}
					if (_tMsg.getData1().equals("shutdown")) {
						running = false;
					}
					if (_tMsg.getData1().equals("makenpc")) {
						//CharacterDto tmpCharDto = new CharacterDto();
						//tmpCharDto.setOwner(1); // an npc will belong to the world account, for database purposes
						//long tmpOwner = tmpCharDto.getOwner();
						//world.getNetObjectManager().addNetObject(new Avatar(), 0, 0, tmpCharDto.getOwner()); // add object to world
						
						//ThreadMessage msg = new ThreadMessage(ThreadMessage.FROM_SIMULATOR, ThreadMessage.TYPE_GET, tmpCharDto); // request the character sheet
						//dispatchDatabaseThreadMessage(msg);
					}
				}
			}
		}
	}

	public World getActiveWorld() {
		return world;
	}
	/*public Server getServer() {
		return server;
	}*/
	@Override
	public void run() {
		running = true;
		world = new World(this); 
		while(running) {
			flushThreadMessages();
			world.simulate();
		}
		System.out.println("Simulator: the world has died rip");
		return;
	}
}
