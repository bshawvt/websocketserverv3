package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import simulator.netobjects.NetObject;

public class QuadTree {
	
	
	private int levels = 3;
	private int level = 0;

	private QuadTree root = null;
	private QuadTree parent = null;
	private QuadTree ne = null;
	private QuadTree nw = null;
	private QuadTree se = null;
	private QuadTree sw = null;
	private ArrayList<ObjectBoundingBox> objects = null;
	
	private String name = null;
	
	public int x = 0;
	public int y = 0;
	public int width = 200;
	public int height = 200;
	
	// debug with graphics
	public QuadTree(int x, int y, int width, int height, ArrayList<ObjectBoundingBox> objects, Graphics g) {
		this.objects = objects;
		this.root = this;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = "root";
		
		g.drawRect(x, y, width, height);
		subdivide(g, 0);
		

	}
	public QuadTree(QuadTree parent, int x, int y, int width, int height, int level, Graphics g, Color color, String name) {
		
			//System.out.println("made quad for " + name);
		if (parent == null) 
			return;
		
		this.root = parent.root;
		this.name = name;
		
		
		this.width = width; //parent.width / 2;
		this.height = width; //parent.height / 2;
		
		this.x = x;
		this.y = y;
		
		//this.currentLevel = parent.currentLevel + 1;
		
		
		g.setColor(new Color((int) Math.floor(Math.random() * (255 * 255 * 255))));
		
		g.drawRect(x, y, width, height);
		g.setColor(color);
		g.fillRect(x, y, width, height);
		
		if (level < levels)
			subdivide(g, level);
		
		
	}	
	public void subdivide(Graphics g, int level) {
		int halfWidth = width/2;
		int halfHeight = height/2;
		
		ne = new QuadTree(this, x, 					y, 					halfWidth, halfHeight, level+1, g, new Color(255, 0, 0), 	"ne" ); // north east
		nw = new QuadTree(this, x + halfWidth, 		y, 					halfWidth, halfHeight, level+1, g, new Color(0, 255, 0), 	"nw" ); // north west
		se = new QuadTree(this, x, 					y + halfHeight, 	halfWidth, halfHeight, level+1, g, new Color(0, 0, 255), 	"se" ); // south east
		sw = new QuadTree(this, x + halfWidth, 		y + halfHeight, 	halfWidth, halfHeight, level+1, g, new Color(255, 255, 0), "sw" ); // south west
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
