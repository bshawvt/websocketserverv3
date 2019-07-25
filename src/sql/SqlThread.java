package sql;

import java.util.concurrent.LinkedBlockingQueue;

import dtos.CharacterDto;
import dtos.UserAccountDto;
import simulator.NetObject;
import simulator.entities.Avatar;
import thread.DatabaseMessage;
import thread.SimulatorMessage;
import thread.ThreadMessage;
import util.L;

public class SqlThread implements Runnable {

	private final LinkedBlockingQueue<ThreadMessage> _tMsgQueueSim;// me
	private final LinkedBlockingQueue<ThreadMessage> _tMsgQueueSrv;// = null;
	private final LinkedBlockingQueue<ThreadMessage> _tMsgQueueDb;
	private final SqlWriter sqlClient;
	
	public SqlThread(LinkedBlockingQueue<ThreadMessage> msgQueueSim, LinkedBlockingQueue<ThreadMessage> msgQueueSrv, LinkedBlockingQueue<ThreadMessage> msgQueueDb) {
		_tMsgQueueSrv = msgQueueSrv;
		_tMsgQueueSim = msgQueueSim;
		_tMsgQueueDb = msgQueueDb;
		
		sqlClient = new SqlWriter();
	}
	public synchronized void dispatchServerThreadMessage(ThreadMessage msg) {
		_tMsgQueueSrv.offer(msg);
	}
	public synchronized void dispatchSimulatorThreadMessage(ThreadMessage msg) {
		_tMsgQueueSim.offer(msg);
	}
	
	public synchronized void flushThreadMessages() {
		ThreadMessage _tMsg = null;
		while((_tMsg = _tMsgQueueDb.poll()) != null) {
			// todo: 
			if (_tMsg.getType() == ThreadMessage.TYPE_GET) {

				if (_tMsg.getFrom() == ThreadMessage.FROM_SIMULATOR) {
					
					System.out.println("SqlWriter: character dto requested");
					
					long ownerid = _tMsg.getDbMessage().getCharacterDto().getOwner();
					if (ownerid > 0) {
						long tmpClientId = _tMsg.getDbMessage().getCharacterDto().getClientId();
						CharacterDto tmpDto = sqlClient.getCharacterDtoFromOwnerId(ownerid);
						if (tmpDto == null) { // create the character if one does not exist for this user
							System.out.println("SqlWriter: creating new character for ownerid=" + ownerid);
							tmpDto = new CharacterDto(_tMsg.getDbMessage().getCharacterDto());
							tmpDto.setOwner(ownerid);
							tmpDto.setName("");
							sqlClient.createCharacter(tmpDto);
						}
						tmpDto.setClientId(tmpClientId);
												
						
						dispatchSimulatorThreadMessage(new ThreadMessage(ThreadMessage.FROM_DATABASE, 
								ThreadMessage.TYPE_GET, 
								new DatabaseMessage(tmpDto) ));
					}
				}
				else if (_tMsg.getFrom() == ThreadMessage.FROM_SERVER) {
					L.d("SqlThread: thread message from server..");
					DatabaseMessage msg = _tMsg.getDbMessage();
					if (_tMsg.getDbMessage().getUserAccountDto() != null) {
						L.d("request for user account initiated");
						UserAccountDto dto = sqlClient.getUserAccountOnVerify(msg.getUserAccountDto().getUsername(), msg.getUserAccountDto().getTmpData());
						dispatchServerThreadMessage(new ThreadMessage(ThreadMessage.FROM_DATABASE, ThreadMessage.TYPE_GET, new DatabaseMessage(dto, _tMsg.getDbMessage().getCharacterDto())));
					}
					//String username = _tMsg.getDatabaseMessage().getUserAccountDto().getUsername();
					//String password = _tMsg.getDatabaseMessage().getUserAccountDto().getTmpData();
					//dispatchServerThreadMessage(new ThreadMessage(ThreadMessage.FROM_DATABASE, ThreadMessage.TYPE_GET, sqlClient.getUserAccountOnVerify(username, password)));
				}
			} else if (_tMsg.getType() == ThreadMessage.TYPE_SET) {
				if (_tMsg.getFrom() == ThreadMessage.FROM_SIMULATOR) {
					sqlClient.saveCharacterDto(_tMsg.getDbMessage().getCharacterDto());
				}
				else if (_tMsg.getFrom() == ThreadMessage.FROM_SERVER) {
					if (_tMsg.getDbMessage().isServerStatusUpdate()) {
						sqlClient.insertServerStatus();
					}
			
				}
			}
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			flushThreadMessages();
		}
	}
	
}
