package dtos;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.BlobFactory.MessageLayerModel;
import server.BlobFactory.UpdateBlobModel;
import server.Client;
import simulator.NetObject;
import util.L;

public class CharacterDto {
	
	// persistent character data
	private long id = -1;
	private long owner = 0;
	private String name = null;
	private String description = null;
	private double x = 0.0;
	private double y = 0.0;
	private double z = 0.0;
	private int wisdomLevel = 0;
	private int wisdomPoints = 0;
	private long unsafeExperiencePoints = 0;
	private long safeExperiencePoints = 0;
	private long lifetimeExperiencePoints = 0;
	private int maxHealth = 10;
	private int maxStamina = 10;
	private Date dateOfCreation = null;
	
	// session data
	//
	private long clientId = -1;
	private long netObjectId = -1;
	
	// netobject sync data 
	private double yaw = 0.0;
	private double pitch = 0.0;
	private int health = 0;
	private int stamina = 0;
	private int inputState = 0;
	private int gameState1 = 0;
	
	public CharacterDto() {
		
	}
	public CharacterDto(ResultSet result) throws SQLException {
		// create dto from database result
		// model of db.characters
		setId(result.getLong(1));
		setDateOfCreation(result.getDate(2));
		setOwner(result.getLong(3));
		
		setName(result.getString(4));
		setDescription(result.getString(5));
		
		setX(result.getDouble(6));
		setY(result.getDouble(7));
		setZ(result.getDouble(8));
		
		setWisdomLevel(result.getInt(9));
		setWisdomPoints(result.getInt(10));
		setUnsafeExperiencePoints(result.getLong(11));
		setSafeExperiencePoints(result.getLong(12));
		setLifetimeExperiencePoints(result.getLong(13));
		
		setMaxHealth(result.getInt(14));
		setMaxStamina(result.getInt(15));
		
	}
	public CharacterDto(CharacterDto character) {
		// todo: 
		setId(character.getId());
		setDateOfCreation(character.getDateOfCreation());
		setOwner(character.getOwner());
		
		setName(character.getName());
		setDescription(character.getDescription());
		
		setX(character.getX());
		setY(character.getY());
		setZ(character.getZ());
		
		setWisdomLevel(character.getWisdomLevel());
		setWisdomPoints(character.getWisdomPoints());
		setUnsafeExperiencePoints(character.getUnsafeExperiencePoints());
		setSafeExperiencePoints(character.getSafeExperiencePoints());
		setLifetimeExperiencePoints(character.getLifetimeExperiencePoints());
		
		setMaxHealth(character.getMaxHealth());
		setMaxStamina(character.getMaxStamina());
	}
	public CharacterDto(Client client) {
		setClientId(client.getId());
		setOwner(client.getUserAccountDto().getId());
		L.d("CharacterDto: clientId, ownerId\n"+client.getId() + ", " + client.getUserAccountDto().getId());

	}
	
	public void updateFromMessage(UpdateBlobModel msg) {
		
		setPitch(msg.getPitch());
		setAngle(msg.getForwardAngle());
		setInputState(msg.getState());
		
	}
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getOwner() {
		return owner;
	}
	public void setOwner(long owner) {
		this.owner = owner;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public int getWisdomLevel() {
		return wisdomLevel;
	}
	public void setWisdomLevel(int wisdomLevel) {
		this.wisdomLevel = wisdomLevel;
	}
	public int getWisdomPoints() {
		return wisdomPoints;
	}
	public void setWisdomPoints(int wisdomPoints) {
		this.wisdomPoints = wisdomPoints;
	}
	public long getUnsafeExperiencePoints() {
		return unsafeExperiencePoints;
	}
	public void setUnsafeExperiencePoints(long unsafeExperiencePoints) {
		this.unsafeExperiencePoints = unsafeExperiencePoints;
	}
	public long getSafeExperiencePoints() {
		return safeExperiencePoints;
	}
	public void setSafeExperiencePoints(long safeExperiencePoints) {
		this.safeExperiencePoints = safeExperiencePoints;
	}
	public long getLifetimeExperiencePoints() {
		return lifetimeExperiencePoints;
	}
	public void setLifetimeExperiencePoints(long lifetimeExperiencePoints) {
		this.lifetimeExperiencePoints = lifetimeExperiencePoints;
	}
	public int getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	public int getMaxStamina() {
		return maxStamina;
	}
	public void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
	}
	public Date getDateOfCreation() {
		return dateOfCreation;
	}
	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	public double getAngle() {
		return yaw;
	}
	public void setAngle(double angle) {
		this.yaw = angle;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getStamina() {
		return stamina;
	}
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public long getNetObjectId() {
		return netObjectId;
	}

	public void setNetObjectId(long netObjectId) {
		this.netObjectId = netObjectId;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public int getInputState() {
		return inputState;
	}
	public void setInputState(int inputState) {
		this.inputState = inputState;
	}
	public int getGameState1() {
		return gameState1;
	}
	public void setGameState1(int gameState1) {
		this.gameState1 = gameState1;
	}
	public double getPitch() {
		return pitch;
	}
	public void setPitch(double pitch) {
		this.pitch = pitch;
	}
	
}
