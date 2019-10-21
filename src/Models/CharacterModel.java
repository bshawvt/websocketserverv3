package Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class CharacterModel {
	private long character_id;
	private long character_owner;
	
	private double x;
	private double y;
	private double z;
	
	private String character_name;
	private String character_description;
	
	private int level;
	private int levelPoints;
	
	private long safeExperience;
	private long unsafeExperience;
	
	private int health_max;
	private int health_current;
	
	private int stamina_max;
	private int stamina_current;
	
	private int food_current;
	private int drink_current;
	
	private Date characterCreationDate;
	

	
	public CharacterModel(ResultSet set) throws SQLException {
		this.character_id = set.getLong("character_id");
		this.character_owner = set.getLong("character_owner");

		this.x = set.getDouble("x");
		this.y = set.getDouble("y");
		this.z = set.getDouble("z");

		this.character_name = set.getString("character_name");
		this.character_description = set.getString("character_description");

		this.level = set.getInt("level");
		this.levelPoints = set.getInt("levelPoints");

		this.safeExperience = set.getLong("safeExperience");
		this.unsafeExperience = set.getLong("unsafeExperience");

		this.health_max = set.getInt("health_max");
		this.health_current = set.getInt("health_current");

		this.stamina_max = set.getInt("stamina_max");
		this.stamina_current = set.getInt("stamina_current");
		
		this.food_current = set.getInt("food_current");
		this.drink_current = set.getInt("drink_current");
		
		this.characterCreationDate = set.getDate("characterCreationDate");
	}
	
	
	public CharacterModel(CharacterModel model) {
		this.character_id = model.getCharacterId();
		this.character_owner = model.getCharacterOwner();

		this.x = model.getX();
		this.y = model.getY();
		this.z = model.getZ();

		this.character_name = model.getCharacterName();
		this.character_description = model.getCharacterDescription();

		this.level = model.getLevel();
		this.levelPoints = model.getLevelPoints();

		this.safeExperience = model.getSafeExperience();
		this.unsafeExperience = model.getUnsafeExperience();

		this.health_max = model.getHealthMax();
		this.health_current = model.getHealthCurrent();

		this.stamina_max = model.getStaminaMax();
		this.stamina_current = model.getStaminaCurrent();

		this.food_current = model.getFoodCurrent();
		this.drink_current = model.getDrinkCurrent();

		this.characterCreationDate = model.getCharacterCreationDate();
	}


	public long getCharacterId() { return this.character_id; }
	public long getCharacterOwner() { return this.character_owner; }

	public double getX() { return this.x; }
	public double getY() { return this.y; }
	public double getZ() { return this.z; }

	public String getCharacterName() { return this.character_name; }
	public String getCharacterDescription() { return this.character_description; }

	public int getLevel() { return this.level; }
	public int getLevelPoints() { return this.levelPoints; }

	public long getSafeExperience() { return this.safeExperience; }
	public long getUnsafeExperience() { return this.unsafeExperience; }

	public int getHealthMax() { return this.health_max; }
	public int getHealthCurrent() { return this.health_current; }

	public int getStaminaMax() { return this.stamina_max; }
	public int getStaminaCurrent() { return this.stamina_current; }
	
	public int getFoodCurrent() { return this.food_current; }
	public int getDrinkCurrent() { return this.drink_current; }

	public Date getCharacterCreationDate() { return this.characterCreationDate; }
	
	
	
}
