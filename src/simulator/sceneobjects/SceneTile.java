package simulator.sceneobjects;

import shared.BoundingBox;
import simulator.World;
import simulator.WorldLoader.WorldLoaderObject.WorldSceneObject.WorldSceneObjectArgs;

public class SceneTile extends SceneObject {
	

	public int type = SceneObject.Types.Tile;
	public SceneTile() {
		this.bb = new BoundingBox(new double[] {Math.random() * 200, Math.random() * 200, 0}, new double[] {Math.random() * 15, Math.random() * 15, 0});
	}
	public SceneTile(WorldSceneObjectArgs args) {
		super(args, SceneObject.Types.Tile);
	}
	public SceneTile(SceneObject obj) {
		super(obj);
		type = SceneObject.Types.Tile;
	}
	
	public SceneTile(int width, int height) {
		this.bb = new BoundingBox(new double[] {Math.random() * width, Math.random() * height, 0}, new double[] {Math.random() * 15, Math.random() * 15, 0});
	}
	@Override
	public void step(World world, double dt) {
		// TODO Auto-generated method stub
		
	}
}