package database;

import java.util.ArrayList;
import java.util.HashMap;

import Models.CharacterModel;

public class Cache {
	public HashMap<Integer, ArrayList<CharacterModel>> characters;
	public Cache() {
		this.characters = new HashMap<>();
	}
}
