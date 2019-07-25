package server;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import Main.Config;
import dtos.CharacterDto;
import dtos.UserAccountDto;
import server.BlobFactory.AuthBlobModel;
import server.BlobFactory.BlobType;
import server.BlobFactory.ChatBlobModel;
import server.BlobFactory.CommandBlobModel;
import server.BlobFactory.CommandType;
import server.BlobFactory.CreateBlobModel;
import server.BlobFactory.MessageLayerModel;
import server.BlobFactory.PingBlobModel;
import server.BlobFactory.ProtoLayerModel;
import server.BlobFactory.ProtoModel;
import server.BlobFactory.UpdateBlobModel;
import server.BlobFactory.UpdateType;
import server.Client.State;
import simulator.NetObjectSnapshot;
import thread.DatabaseMessage;
import thread.SimulatorMessage;
import thread.ThreadMessage;
import util.L;


public class ClientMessage {
	
	private Client client;
	private ClientManager clientManager;

	public ClientMessage(ClientManager clientManager, Client client, String message) {
		this.client = client;
		this.clientManager = clientManager;
		deserializeBlobs(message);
		
	}
	public static String serializeMessagePing(long id, long l) {
		long time = System.nanoTime()/1000000;
		String json = "{\"type\":" + BlobType.TYPE_PING + ",\"id\":" + id + ",\"timestamp\":" + time + ", \"prevLatency\":" + l + "}";
		return json;
	}
	public static String serializeMessageCommand(long type, long issuerId, long targetId) {
		String json = "{\"type\":" + BlobType.TYPE_COMMAND + ",\"cmdType\":" + type + ",\"issuerId\":" + issuerId + ",\"targetId\":" + targetId + "}";
		return json;
	}
	public static String serializeMessageDebug(String msg) {
		String json = "{\"type\":" + BlobType.TYPE_NONE + ",\"data\":\"" + msg + "\"}";
		return json;
	}
	public static String serializeMessageContent(int type, String src, String start, String end) {
		String json = "{\"type\":" + BlobType.TYPE_CONTENT + ",\"contentType\":" + type + ",\"src\":\"" + src +"\",\"start\":" + start + ",\"end\":" + end + "}";
		//System.out.println(json);
		//"{\"type\":" + BlobType.TYPE_UPDATE + ",\"updateType\":" + UpdateType.TYPE_REMOVE + ",\"id\":-1}";
		return json;
	}
	public static String serializeMessageNetObjectReset() {
		String json = "{\"type\":" + BlobType.TYPE_UPDATE + ",\"updateType\":" + UpdateType.TYPE_REMOVE + ",\"id\":-1}";
		return json;
	}
	public static String serializeMessageRemoveNetObject(SimulatorMessage msg) {
		
		String json = "{\"type\":" + BlobType.TYPE_UPDATE + ",\"updateType\":" + UpdateType.TYPE_REMOVE + ",\"id\":" + msg.getCharacterDto().getNetObjectId() + "}";
		return json;
	}
	public static String serializeMessageCreateNetObject(SimulatorMessage msg) {
			
		String position = "[" + msg.getCharacterDto().getX() + "," + msg.getCharacterDto().getY() + "," + msg.getCharacterDto().getZ() + "]";
		int keyState = msg.getCharacterDto().getInputState();
		double health = msg.getCharacterDto().getHealth();
		double yawSpeed = msg.getCharacterDto().getAngle();
		//String json = "{\"f\": {\"size\": 1, \"blobs\": [{\"type\": " + BlobType.TYPE_UPDATE + ", \"updateType\": " + UpdateType.TYPE_CREATE + ", \"createType\": " + 0 + ", \"id\": " + netObj.getId() + ", \"radius\": " + radius + ", \"position\": " + position + ", \"health\": " + health + ", \"keyState\": " + keyState + " }]}}";
		String json = "{\"type\":" + BlobType.TYPE_UPDATE + ",\"updateType\":" + UpdateType.TYPE_CREATE + ",\"owner\":" + 0 + ",\"id\":" + msg.getCharacterDto().getNetObjectId() + ",\"position\":" + position + ",\"yawSpeed\":" + yawSpeed + ",\"health\":" + health + ",\"keyState\":" + keyState + "}";
		return json;
	}
	public static String serializeMessageCreateNetObject(SimulatorMessage msg, int isTheGuy) {
		
		String position = "[" + msg.getCharacterDto().getX() + "," + msg.getCharacterDto().getY() + "," + msg.getCharacterDto().getZ() + "]";
		int keyState = msg.getCharacterDto().getInputState();
		double health = msg.getCharacterDto().getHealth();
		double yawSpeed = msg.getCharacterDto().getAngle();
		//String json = "{\"f\": {\"size\": 1, \"blobs\": [{\"type\": " + BlobType.TYPE_UPDATE + ", \"updateType\": " + UpdateType.TYPE_CREATE + ", \"createType\": " + 0 + ", \"id\": " + netObj.getId() + ", \"radius\": " + radius + ", \"position\": " + position + ", \"health\": " + health + ", \"keyState\": " + keyState + " }]}}";
		String json = "{\"type\":" + BlobType.TYPE_UPDATE + ",\"updateType\":" + UpdateType.TYPE_CREATE + ",\"owner\":" + 1 + ",\"id\":" + msg.getCharacterDto().getNetObjectId() + ",\"position\":" + position + ",\"yawSpeed\":" + yawSpeed + ",\"health\":" + health + ",\"keyState\":" + keyState + "}";
		return json;
	}
	public static String serializeMessageSyncNetObject(NetObjectSnapshot snap, int thatGuy) {
		double angle = snap.getForwardAngle();

		int keyState = snap.getKeyState();
		int gameState1 = snap.getGameState1();
		//L.d("gameState1=" + gameState1);
		double health = snap.getHealth();
		//double yaw = snap.getNetObjectBase().getYaw();
		double radius = snap.getRadius();
		double pitch = snap.getPitch();
		
		String json = "";
		
		json = "{\"type\": " + BlobType.TYPE_UPDATE + 
				",\"updateType\":" + UpdateType.TYPE_SYNC;
		
		if (snap.getPosition().length == 3) {			 
			String position = "[" + snap.getPosition()[0] + "," + snap.getPosition()[1] + ", " + snap.getPosition()[2] + "]";
			json += ",\"position\":" + position;
			json += (thatGuy == 1 ? ",\"owner\":" + thatGuy : "");
		}
		
		json += ",\"id\":" + snap.getNetObjectId(); 
		json += ",\"radius\":" + radius;		
		json += ",\"health\":" + health; 
		json += ",\"angle\":" + angle;
		json += ",\"pitch\":" + pitch;
		json += ",\"stamina\":" + snap.getStamina();
		json += ",\"keyState\":" + keyState; 
		json += ",\"gameState1\":" + gameState1 + "}";
		return json;
		
	}

	private void deserializeBlobs(String msg) {
		
		/*
		var blobsStructure = {
			f: { // proto layer
				size: 0, // size of blobs
				blobs: [ // array with individual messages
					{ // message layer, items are expected to match patterns defined by the server
						type: 0, // message type, eg TYPE_UPDATE,
						message: {} // models after a matching type. eg, no x/y/z coordinates in a chat message
					} 
				]
			}
		}
		*/
		//L.d(msg);
		Gson gson = new Gson();
		ProtoModel proto = null;
		try {
			proto = gson.fromJson(msg, ProtoModel.class);
		}
		catch (JsonParseException e) {
			L.e("ClientMessage: deserializeBlobs: invalid message model: " + e.getMessage());
			return;
		}
	
		if (proto.getProtoLayer() == null) {
			L.e("ClientMessage: deserializeBlobs: protoLayer is null!");
			return;
		}
		
		ProtoLayerModel protoLayer = proto.getProtoLayer();
		if (protoLayer.getSize() != protoLayer.getBlobs().size()) {
			L.e("ClientMessage: deserializeBlobs: protoLayer size does not match length of blobs");
			return;
		}
		if (protoLayer.getSize() > 40) {
			L.e("ClientMessage: deserializeBlobs: protoLayer size is much too large");
			return;
		}

		for(int i = 0; i < protoLayer.getSize(); i++) {

			MessageLayerModel messageLayer = protoLayer.getBlobs().get(i);

			if (messageLayer.getType() == BlobType.TYPE_AUTH) { //
				if (messageLayer.getAuth() instanceof AuthBlobModel) {
					if (messageLayer.getAuth() == null || messageLayer.getAuth().getUsername() == null || messageLayer.getAuth().getPassword() == null) {
						L.e("ClientMessage: deserializeBlobs: auth: getAuth is invalid");
						break; // should I skip the remaining messages when an inconsistency is found?
					}
					if (clientManager.getClientByUsername(messageLayer.getAuth().getUsername()) != null) {
						client.immediateSend("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":0,\"message\": \"This account has been locked\"}");
						client.setRemoved(true);
						return;
					}
					if (messageLayer.getAuth().getVersion() == null || messageLayer.getAuth().getVersion().compareTo(Config.CLIENT_VERSION) != 0) {
						client.immediateSend("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":0,\"message\": \"The client is out of date\"}");
						client.setRemoved(true);
						return;
					}
					UserAccountDto user = new UserAccountDto();
					user.setUsername(messageLayer.getAuth().getUsername());
					user.setTmpData(messageLayer.getAuth().getPassword());
					client.setUserAccountDto(user);
					
					client.setCharacterDto(new CharacterDto(client));
					
					client.setUnauthenticatedUsername(messageLayer.getAuth().getUsername());
					
					//client.setUsername(messageLayer.getAuth().getUsername());
					clientManager.getServerThread().dispatchSqlThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, ThreadMessage.TYPE_GET, new DatabaseMessage(user, client.getCharacterDto())));
					
					
					/*UserAccountDto user = null;
					if ((user = clientManager.getSqlClient().getUserAccountOnVerify(messageLayer.getAuth().getUsername(), messageLayer.getAuth().getPassword())) != null) {//(messageLayer.getAuth().getUsername().length() > 3) { 
						Client _tClient = clientManager.getClientByUsername(messageLayer.getAuth().getUsername());
						if (user.isLocked() == true || _tClient != null) { // if this user account is already connected boot both connections out because I dont know how to handle this situation right now
							long _out = client.immediateSend("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":0,\"message\": \"This account has been locked\"}");
							//clientManager.addBandwidthOut(_out);
							//client.getConnection().close();
							client.setRemoved(true);
							return;
						}

						client.setUserAccountDto(user);
						client.setUsername(messageLayer.getAuth().getUsername());
						client.setState(State.READY);
						
						// on auth, create dto with client id and account owner id
						client.setCharacterDto(new CharacterDto(client));
						
						clientManager.getServerThread().dispatchSimulationThreadMessage(new ThreadMessage(ThreadMessage.FROM_SERVER, 
								ThreadMessage.TYPE_CREATE, 
								new SimulatorMessage(client.getCharacterDto()) ));
						client.pushMessage("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":1,\"message\": \"logged in as " + messageLayer.getAuth().getUsername() + "\"}");
						clientManager.printAll("Someone has connected...");
						continue;
					}
					else {
						L.e("ClientMessage: deserializeBlob: auth: someone tried logging in as: " + messageLayer.getAuth().getUsername() + " but had an bad details!");
						long _out = client.immediateSend("{\"type\":" + BlobType.TYPE_AUTH + ",\"result\":0,\"message\": \"Failed to login. Try again!\"}");
						//clientManager.addBandwidthOut(_out);
						client.getConnection().close();
						return;
					}*/
				}
			}
			
			/*else if (messageLayer.getType() == BlobType.TYPE_CREATE) { // TODO: account creation has been migrated to a website
				if (messageLayer.getCreate() instanceof CreateBlobModel) {
					if (messageLayer.getCreate() == null) {
						L.e("ClientMessage: deserializeBlobs: create: getCreate is null");
						continue;
					}
					if (clientManager.getSqlClient().isUsernameInUse(messageLayer.getCreate().getUsername())) {
						long _out = client.immediateSend("{\"type\":" + BlobType.TYPE_CREATE + ",\"result\":0,\"message\": \"username taken\"}");
						//clientManager.addBandwidthOut(_out);
					}
					else if (clientManager.getSqlClient().isEmailInUse(messageLayer.getCreate().getEmail())) {
						long _out = client.immediateSend("{\"type\":" + BlobType.TYPE_CREATE + ",\"result\":0,\"message\": \"email already in use\"}");
						//clientManager.addBandwidthOut(_out);
					} else {
						clientManager.getSqlClient().createAccount(messageLayer.getCreate().getUsername(), messageLayer.getCreate().getPassword(), messageLayer.getCreate().getEmail());
						long _out = client.immediateSend("{\"type\":" + BlobType.TYPE_CREATE + ",\"result\":1,\"message\": \"account created successfully.\\nyou may login now.\"}");
						//clientManager.addBandwidthOut(_out);
					}
					client.getConnection().close();
				}
			}*/
			
			else if (messageLayer.getType() == BlobType.TYPE_CHAT) {
				if (messageLayer.getChat() instanceof ChatBlobModel) {
					if (messageLayer.getChat() == null) {
						L.e("ClientMessage: deserializeBlobs: chat: getChat is null");
						continue;
					}
					if (client.getState() == State.READY) {
						String _name = (messageLayer.getChat().getMessage().toLowerCase().contains(client.getUnauthenticatedUsername().toLowerCase()) ? client.getUnauthenticatedUsername() : "unknown");
						clientManager.printAll(":" + client.getId() + ":" + _name + ": " + messageLayer.getChat().getMessage());
						L.d(":" + client.getId() + ":" + _name + ": " + messageLayer.getChat().getMessage());
						continue;
					}
				}
			}
			
			else if (messageLayer.getType() == BlobType.TYPE_PING) {
				if (messageLayer.getPing() instanceof PingBlobModel) {
					if (messageLayer.getPing() == null) {
						L.e("ClientMessage: deserializeBlobs: getPing is null");
						continue;
					}
					if (client.getState() == State.READY) {
						//L.d("received pong...");
						if (messageLayer.getPing().getId() == client.getCurrentPingId()) {
							long time = System.nanoTime()/1000000;
							long dt = time - client.getPingTimeStart();
							client.setLatency(dt);
							client.setLastPongTime(time);
							//L.d("processed pong");
						}
						continue;
					}
				}
			}
			
			else if (messageLayer.getType() == BlobType.TYPE_UPDATE) {
				if (messageLayer.getUpdate() instanceof UpdateBlobModel) {
					if (messageLayer.getUpdate() == null) {
						continue;
					}
					if (client.getState() == State.READY) {
						if (messageLayer.getUpdate().getUpdateType() == UpdateType.TYPE_PLAYERSTATE) {
							client.setRequestId(protoLayer.getRequest());
							
							client.getCharacterDto().updateFromMessage(messageLayer.getUpdate());
							
							ThreadMessage threadMsg = new ThreadMessage(ThreadMessage.FROM_SERVER, ThreadMessage.TYPE_SET, new SimulatorMessage(client.getCharacterDto()));
							clientManager.getServerThread().dispatchSimulationThreadMessage(threadMsg);
							
						}
						continue;
					}
				}
			}
			
			else if (messageLayer.getType() == BlobType.TYPE_COMMAND) {
				if (messageLayer.getCommand() instanceof CommandBlobModel) {
					if (messageLayer.getCommand() == null) {
						continue;
					}
					if (client.getState() == State.READY) {
						if (messageLayer.getCommand().getCommandType() == CommandType.TYPE_NONE) {
							L.d("received a command from client.");
							//client.setRequestId(protoLayer.getRequest());
							
							//client.getCharacterDto().updateFromMessage(messageLayer.getCommand());
							
							//ThreadMessage threadMsg = new ThreadMessage(ThreadMessage.FROM_SERVER, ThreadMessage.TYPE_SET, new SimulatorMessage(client.getCharacterDto()));
							//clientManager.getServerThread().dispatchSimulationThreadMessage(threadMsg);
							
						}
						continue;
					}
				}
			}
			/*// copy pasta template
			else if (messageLayer.getType() == BlobType.TYPE_UPDATE) {
				if (messageLayer.getUpdate() instanceof UpdateBlobModel) {
					if (messageLayer.getUpdate() == null) {
						continue;
					}
					if (client.getState() == State.READY) {
						if (messageLayer.getUpdate().getUpdateType() == UpdateType.TYPE_PLAYERSTATE) {
							client.setRequestId(protoLayer.getRequest());
							
							client.getCharacterDto().updateFromMessage(messageLayer.getUpdate());
							
							ThreadMessage threadMsg = new ThreadMessage(ThreadMessage.FROM_SERVER, ThreadMessage.TYPE_SET, new SimulatorMessage(client.getCharacterDto()));
							clientManager.getServerThread().dispatchSimulationThreadMessage(threadMsg);
							
						}
						continue;
					}
				}
			}*/
			else {
				L.e("ClientMessage: deserializeBlobs: bad message layer type");
				break;
			}	
		}
	}
}