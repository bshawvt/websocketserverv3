package thread;

import dtos.CharacterDto;
import dtos.UserAccountDto;

public class DatabaseMessage {
	
	private final CharacterDto characterDto;
	private final UserAccountDto accountDto;
	private final boolean serviceServerStatus;
	//private final UserAccountDto accountDto;
	
	public DatabaseMessage() {
		this.characterDto = null;
		this.serviceServerStatus = false;
		this.accountDto = null;
	}
	public DatabaseMessage(boolean serviceUpdate) {
		this.serviceServerStatus = serviceUpdate;
		
		this.characterDto = null;
		this.accountDto = null;
	}
	public DatabaseMessage(UserAccountDto dto) {
		this.accountDto = dto;
		
		this.serviceServerStatus = false;
		this.characterDto = null;
	}
	public DatabaseMessage(UserAccountDto dto, CharacterDto charDto) {
		this.accountDto = dto;
		this.characterDto = charDto;
		
		this.serviceServerStatus = false;
	}
	public DatabaseMessage(CharacterDto dto) {
		this.characterDto = dto;
		
		this.serviceServerStatus = false;
		this.accountDto = null;
	}
	
	public boolean isServerStatusUpdate() { 
		return serviceServerStatus; 
	}
	public CharacterDto getCharacterDto() {
		return characterDto;
	}
	public UserAccountDto getUserAccountDto() {
		return accountDto;
	}

	

}
