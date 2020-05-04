package simulator.sceneobjects;

import simulator.World;
import simulator.WorldLoader.WorldLoaderObject.WorldSceneObject.WorldSceneObjectArgs;

public class SceneTile extends SceneObject {
	

	public int type = SceneObject.Types.Tile;
	public SceneTile(WorldSceneObjectArgs args) {
		super(args, SceneObject.Types.Tile);
	}
	public SceneTile(SceneObject obj) {
		super(obj);
		type = SceneObject.Types.Tile;
	}
	
	@Override
	public void step(World world, double dt) {
		// TODO Auto-generated method stub
		
	}
}