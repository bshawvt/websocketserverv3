package tools;

import java.awt.Graphics;
import java.util.ArrayList;

import simulator.netobjects.NetObject;

public class QuadTree {
	
	QuadTree divisions[] = {null, null, null, null};
	
	/* the initial QuadTree constructor */
	public QuadTree(ArrayList<ObjectBoundingBox> objects, Graphics g) {
		int x = 0;
		int y = 0;
		int width = 200;
		int height = 200;
		
		
		
	}
	/* */
	public QuadTree() {
		
	}
	
	/*package simulator;

	import java.util.ArrayList;
	import java.util.Iterator;
	import java.util.function.Consumer;

	import simulator.netobjects.NetObject;

	public class QuadTree {

		private ArrayList<NetObject> objectsList;
		public QuadTree(ArrayList<NetObject> ref) {
			this.objectsList = ref;
		}
		
		public ArrayList<NetObject> get(double[] position) {
			//ArrayList<NetObject> objects = new ArrayList<>();
			return objectsList;
		}
		public ArrayList<NetObject> getClients(double[] position) {
			ArrayList<NetObject> objects = new ArrayList<>();
			Iterator<NetObject> it = objectsList.iterator();
			while (it.hasNext()) {
				NetObject netObject = it.next();
				if (netObject.clientId != -1) {
					objects.add(netObject);
				}			
			}
			return objects;
		}
		

	}*/

}
