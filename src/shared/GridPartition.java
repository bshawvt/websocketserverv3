package shared;

import java.util.ArrayList;

import simulator.sceneobjects.SceneObject;

public class GridPartition {
	public SceneObject[] cells = null;
	public GridPartition(int size, ArrayList<SceneObject>objects, int ratio) {
		cells = new SceneObject[100];
	}
}
