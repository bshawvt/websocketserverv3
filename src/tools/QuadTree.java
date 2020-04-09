package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import simulator.netobjects.NetObject;

public class QuadTree {
	
	
	private int levels = 4;
	private int level = 0;

	private ArrayList<ObjectBoundingBox> container = null;
	private QuadTree root = null;
	private QuadTree parent = null;
	private QuadTree ne = null;
	private QuadTree nw = null;
	private QuadTree se = null;
	private QuadTree sw = null;
	
	private String name = null;
	
	public int x = 0;
	public int y = 0;
	public int width = 200;
	public int height = 200;
	
	// debug with graphics
	public QuadTree(int x, int y, int width, int height, ArrayList<ObjectBoundingBox> objects, Graphics g) {

		this.root = this;
		this.container = new ArrayList<>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = "root";
		
		g.drawRect(x, y, width, height);
		//subdivide(g, 0);
		//insert(objects);
		objects.forEach((e) -> {
			insert(e, g);
		});
		

	}
	public QuadTree(QuadTree parent, int x, int y, int width, int height, int level, Graphics g, Color color, String name) {
		
		//System.out.println("made quad for " + name);
		if (parent == null) 
			return;
		
		this.root = parent.root;
		
		this.container = new ArrayList<>();
		this.name = name;
		this.level = level;
		
		
		this.width = width; //parent.width / 2;
		this.height = width; //parent.height / 2;
		
		this.x = x;
		this.y = y;
		
		//this.currentLevel = parent.currentLevel + 1;
		
		
		g.setColor(new Color(0, 0, 0));
		//g.setColor(new Color((int) Math.floor(Math.random() * (255 * 255 * 255))));
		
		g.drawRect(x, y, width, height);
		//g.setColor(color);
		//g.fillRect(x, y, width, height);
		//insert(objects);
		
	}
	public QuadTree(QuadTree parent, ObjectBoundingBox bb, int level, Graphics g, Color color, String name) {
			
		//System.out.println("made quad for " + name);
		if (parent == null) 
			return;
		
		this.root = parent.root;
		
		this.container = new ArrayList<>();
		this.name = name;
		this.level = level;
		
		this.x = (int) Math.floor(bb.x);
		this.y = (int) Math.floor(bb.y);
		
		this.width = (int) Math.floor(bb.width); //parent.width / 2;
		this.height = (int) Math.floor(bb.height); //parent.height / 2;
		
		
		
		//this.currentLevel = parent.currentLevel + 1;
		
		
		g.setColor(new Color(0, 0, 0));
		//g.setColor(new Color((int) Math.floor(Math.random() * (255 * 255 * 255))));
		
		g.drawRect(x, y, width, height);
		//g.setColor(color);
		//g.fillRect(x, y, width, height);
		//insert(objects);
		
	}
	/*public void subdivide(Graphics g, int level) {
		int halfWidth = width/2;
		int halfHeight = height/2;
		
		ne = new QuadTree(this, x, y, 							halfWidth, halfHeight, level+1, g, new Color(255, 0, 0), 	"ne" ); // north east
		nw = new QuadTree(this, x + halfWidth, y, 				halfWidth, halfHeight, level+1, g, new Color(0, 255, 0), 	"nw" ); // north west
		se = new QuadTree(this, x, y + halfHeight, 				halfWidth, halfHeight, level+1, g, new Color(0, 0, 255), 	"se" ); // south east
		sw = new QuadTree(this, x + halfWidth, y + halfHeight, 	halfWidth, halfHeight, level+1, g, new Color(255, 255, 0), "sw" ); // south west
		
	}*/
	
	public void insert(ObjectBoundingBox object, Graphics g) {
		System.out.println(level + " of " + levels);
		if (level < levels) {
			int halfWidth = width/2;
			int halfHeight = height/2;
			
			g.setColor(new Color(255, 0, 0));
			g.fillArc((int) object.x, (int) object.y, (int) object.width, (int) object.height, 0, 360);
			
			ObjectBoundingBox neBB = new ObjectBoundingBox(x, 				y,				halfWidth, halfHeight);
			ObjectBoundingBox nwBB = new ObjectBoundingBox(x + halfWidth, 	y, 				halfWidth, halfHeight);
			ObjectBoundingBox seBB = new ObjectBoundingBox(x, 				y + halfHeight, halfWidth, halfHeight);
			ObjectBoundingBox swBB = new ObjectBoundingBox(x + halfWidth, 	y + halfHeight, halfWidth, halfHeight);
			if (ne == null) {
				ne = new QuadTree(this, neBB, level+1, g, new Color(255, 0, 0), "ne");
			}
			if (nw == null) {		
				nw = new QuadTree(this, nwBB, level+1, g, new Color(255, 0, 0), "nw");
			}
			if (se == null) {		
				se = new QuadTree(this, seBB, level+1, g, new Color(255, 0, 0), "se");
			}
			if (sw == null) {		
				sw = new QuadTree(this, swBB, level+1, g, new Color(255, 0, 0), "sw");
			}
			
			boolean added = false;
			// ne
			if (leafContains(neBB, object)) {
				ne.insert(object, g);
				added = true;
			}
			
			// nw
			if (leafContains(nwBB, object)) {
				nw.insert(object, g);
				added = true;
			}
			
			// se
			if (leafContains(seBB, object)) {
				se.insert(object, g);
				added = true;
			}
			
			// sw
			if (leafContains(swBB, object)) {
				sw.insert(object, g);
				added = true;
			}
			System.err.println("was added is " + added);
		}
		else {
			add(object);
		}
	}
	/*public boolean leafContains(int x, int y, int width, int height, ObjectBoundingBox object) {
		ObjectBoundingBox bb = new ObjectBoundingBox(x, y, width, height);
		if (bb.intersect(object)) {
			return true;
		}
		return false;
	}*/
	public boolean leafContains(ObjectBoundingBox leafBB, ObjectBoundingBox objectBB) {

		if (leafBB.intersect(objectBB)) {
			return true;
		}
		return false;
	}
	public void add(ObjectBoundingBox object) {
		System.err.println("added thing");
		
		container.add(object);
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
