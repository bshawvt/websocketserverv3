package shared;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;

import simulator.netobjects.NetObject;

public class NOQuadTree {
	private int maxDepth = 4;
	private int depth = 0;

	private ArrayList<NetObject> container = null;
	private NOQuadTree root = null;
	private NOQuadTree[] divisions = { null, null, null, null};
	
	private BoundingBox bb = null;
	
	public int x = 0;
	public int y = 0;
	public int width = 200;
	public int height = 200;
	
	// drawing
	public int dx = 0;
	public int dy = 0;
	
	public boolean divided = false;
	
	// debug with graphics
	public NOQuadTree(int maxDepth, int dx, int dy, int x, int y, int width, int height, ArrayList<NetObject> objects, Graphics g) {

		this.root = this;
		this.container = new ArrayList<>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.maxDepth = maxDepth;
		this.depth = 0;
		
		this.dx = dx;
		this.dy = dy;
				
		this.bb = new BoundingBox(x, y, width, height);
		
		g.drawRect(dx, dy, width, height);

		objects.forEach((e) -> {
			insert(e, g);
		});
		
	}
	
	/** this constructor should never be used to create the quadtree */
	public NOQuadTree(NOQuadTree parent, BoundingBox bb, int depth, Graphics g, Color color) {

		if (parent == null) 
			return;
		
		this.root = parent.root;
		this.bb = bb;
		
		this.container = new ArrayList<>();
		this.maxDepth = root.maxDepth;
		this.depth = depth;
		
		this.x = (int) Math.floor(bb.x);
		this.y = (int) Math.floor(bb.y);
		this.width = (int) Math.floor(bb.xscale);
		this.height = (int) Math.floor(bb.yscale);
		
		this.dx = root.dx+x;
		this.dy = root.dy+y;
		
		this.divided = false;
		
		g.setColor(new Color(0, 0, 0));
		g.drawRect(dx, dy, width, height);
		
	}
	private void insert(NetObject object, Graphics g) {
		//System.out.println(depth + " of " + maxDepth);
		if (depth < maxDepth) {
			int halfWidth = width/2;
			int halfHeight = height/2;

			// the order is important, the tree goes clockwise starting at NE
			BoundingBox[] bbs = { 
					new BoundingBox(x, y, halfWidth, halfHeight), // ne
					new BoundingBox(x + halfWidth, y, halfWidth, halfHeight), // nw 
					new BoundingBox(x, y + halfHeight, halfWidth, halfHeight), // se
					new BoundingBox(x + halfWidth, y + halfHeight, halfWidth, halfHeight) // sw
			};
			
			for(int i = 0; i < 4; i++) {	
				if (bbs[i].intersect2d(new BoundingBox(object.position, object.bounds))) {
					if (divisions[i] == null) {
						divisions[i] = new NOQuadTree(this, bbs[i], depth+1, g, new Color(255, 0, 0));
					}
					divisions[i].divided = true;
					divisions[i].insert(object, g);
				}
			}
		}
		else {
			container.add(object);
		}
		
		g.setColor(new Color(255, 0, 0));
		g.fillArc(root.dx + (int) object.position[0], root.dy + (int) object.position[1], (int) object.bounds[0], (int) object.bounds[1], 0, 360);
		
	}
	/** recursively calls itself to search for and populate a list reference with all objects within rect */
	private void get(BoundingBox rect, HashSet<NetObject> list, Graphics g) {		
		for(int i = 0; i < 4; i++) {
			if (divisions[i] != null) {
				if (rect.intersect2d(divisions[i].bb)) {
					divisions[i].container.forEach((e) -> {
						g.setColor(new Color(0, 0, 255));
						g.drawArc(root.dx + (int) e.position[0] - 2, root.dy + (int) e.position[1] - 2, (int) e.bounds[0] + 4 , (int) e.bounds[1] + 4, 0, 360);
						if (rect.intersect2d(new BoundingBox(e.position, e.bounds))) {
							list.add(e);
						}
					});
					if (divisions[i].divided) {
						divisions[i].get(rect, list, g);
					}
				}
			}
		}
	}
	/**
	 * 
	 * @param rect
	 * @param g
	 * @return arraylist of objects within rect
	 */
	public HashSet<NetObject> get(BoundingBox rect, Graphics g) {
		HashSet<NetObject> list = new HashSet<>();

		get(rect, list, g);
		
		g.setColor(new Color(255, 0, 255));
		g.drawRect(root.dx + (int) rect.x - 1, root.dy + (int) rect.y - 1, (int) rect.xscale + 2, (int) rect.yscale + 2);
		g.drawRect(root.dx + (int) rect.x, root.dy + (int) rect.y, (int) rect.xscale, (int) rect.yscale);
		return list;
	}
	
	
	// no graphics
	public NOQuadTree(int maxDepth, int x, int y, int width, int height, ArrayList<NetObject> objects) {

		this.root = this;
		this.container = new ArrayList<>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.maxDepth = maxDepth;
		this.depth = 0;
				
		this.bb = new BoundingBox(x, y, width, height);
		
		objects.forEach((e) -> {
			insert(e);
		});
		
	}
	
	/** this constructor should never be used to create the quadtree */
	public NOQuadTree(NOQuadTree parent, BoundingBox bb, int depth) {

		if (parent == null) 
			return;
		
		this.root = parent.root;
		this.bb = bb;
		
		this.container = new ArrayList<>();
		this.maxDepth = root.maxDepth;
		this.depth = depth;
		
		this.x = (int) Math.floor(bb.x);
		this.y = (int) Math.floor(bb.y);
		this.width = (int) Math.floor(bb.xscale);
		this.height = (int) Math.floor(bb.yscale);
		
		this.dx = root.dx+x;
		this.dy = root.dy+y;
		
		this.divided = false;
		
		
	}
	private void insert(NetObject object) {
		//System.out.println(depth + " of " + maxDepth);
		if (depth < maxDepth) {
			int halfWidth = width/2;
			int halfHeight = height/2;

			// the order is important, the tree goes clockwise starting at NE
			BoundingBox[] bbs = { 
					new BoundingBox(x, y, halfWidth, halfHeight), // ne
					new BoundingBox(x + halfWidth, y, halfWidth, halfHeight), // nw 
					new BoundingBox(x, y + halfHeight, halfWidth, halfHeight), // se
					new BoundingBox(x + halfWidth, y + halfHeight, halfWidth, halfHeight) // sw
			};
			
			for(int i = 0; i < 4; i++) {	
				if (bbs[i].intersect2d(new BoundingBox(object.position, object.bounds))) {
					if (divisions[i] == null) {
						divisions[i] = new NOQuadTree(this, bbs[i], depth+1);
					}
					divisions[i].divided = true;
					divisions[i].insert(object);
				}
			}
		}
		else {
			container.add(object);
		}

	}
	/** recursively calls itself to search for and populate a list reference with all objects within rect */
	private void get(BoundingBox rect, HashSet<NetObject> list) {		
		for(int i = 0; i < 4; i++) {
			if (divisions[i] != null) {
				if (rect.intersect2d(divisions[i].bb)) {
					divisions[i].container.forEach((e) -> {
						if (rect.intersect2d(new BoundingBox(e.position, e.bounds))) {
							list.add(e);
						}
					});
					if (divisions[i].divided) {
						divisions[i].get(rect, list);
					}
				}
			}
		}
	}
	/**
	 * 
	 * @param rect
	 * @param g
	 * @return arraylist of objects within rect
	 */
	public HashSet<NetObject> get(BoundingBox rect) {
		HashSet<NetObject> list = new HashSet<>();

		get(rect, list);
		
		return list;
	}
}