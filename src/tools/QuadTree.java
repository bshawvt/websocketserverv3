package tools;

import java.awt.Graphics;
import java.util.ArrayList;

import simulator.netobjects.NetObject;

public class QuadTree {
	public QuadTree() {
		
	}
	public QuadTree(ArrayList<NetObject> objects, Graphics g) {
		
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
