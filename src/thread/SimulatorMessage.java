package thread;

import dtos.CharacterDto;
import dtos.UserAccountDto;
import server.BlobFactory.UpdateBlobModel;
import simulator.WorldFrame;

public class SimulatorMessage {
	
	private final CharacterDto characterDto;
	private final WorldFrame worldFrame;
	private final UpdateBlobModel update;
	//private final UserAccountDto accountDto;
	
	public SimulatorMessage(CharacterDto dto) {
		this.characterDto = dto;
		
		this.worldFrame = null;
		this.update = null;
	}

	public SimulatorMessage(WorldFrame worldFrame) {
		this.worldFrame = worldFrame;
		
		this.characterDto = null;
		this.update = null;
	}

	public CharacterDto getCharacterDto() {
		return characterDto;
	}

	public WorldFrame getWorldFrame() {
		return worldFrame;
	}

	public UpdateBlobModel getUpdate() {
		return update;
	}

	
	
	
	
}
