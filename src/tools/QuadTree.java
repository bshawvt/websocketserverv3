package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;

public class QuadTree {
	private int maxDepth = 4;
	private int depth = 0;

	private ArrayList<ObjectBoundingBox> container = null;
	private QuadTree root = null;
	private QuadTree[] divisions = { null, null, null, null};
	
	private ObjectBoundingBox bb = null;
	
	public int x = 0;
	public int y = 0;
	public int width = 200;
	public int height = 200;
	
	// drawing
	public int dx = 0;
	public int dy = 0;
	
	public boolean divided = false;
	
	// debug with graphics
	public QuadTree(int maxDepth, int dx, int dy, int x, int y, int width, int height, ArrayList<ObjectBoundingBox> objects, Graphics g) {

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
				
		this.bb = new ObjectBoundingBox(x, y, width, height);
		
		g.drawRect(dx, dy, width, height);

		objects.forEach((e) -> {
			insert(e, g);
		});
		
	}
	
	/** this constructor should never be used to create the quadtree */
	public QuadTree(QuadTree parent, ObjectBoundingBox bb, int depth, Graphics g, Color color) {

		if (parent == null) 
			return;
		
		this.root = parent.root;
		this.bb = bb;
		
		this.container = new ArrayList<>();
		this.maxDepth = root.maxDepth;
		this.depth = depth;
		
		this.x = (int) Math.floor(bb.x);
		this.y = (int) Math.floor(bb.y);
		this.width = (int) Math.floor(bb.width);
		this.height = (int) Math.floor(bb.height);
		
		this.dx = root.dx+x;
		this.dy = root.dy+y;
		
		this.divided = false;
		
		g.setColor(new Color(0, 0, 0));
		g.drawRect(dx, dy, width, height);
		
	}
	private void insert(ObjectBoundingBox object, Graphics g) {
		//System.out.println(depth + " of " + maxDepth);
		if (depth < maxDepth) {
			int halfWidth = width/2;
			int halfHeight = height/2;

			// the order is important, quadtree goes clockwise starting at NE
			ObjectBoundingBox[] bbs = { 
					new ObjectBoundingBox(x, y, halfWidth, halfHeight), // ne
					new ObjectBoundingBox(x + halfWidth, y, halfWidth, halfHeight), // nw 
					new ObjectBoundingBox(x, y + halfHeight, halfWidth, halfHeight), // se
					new ObjectBoundingBox(x + halfWidth, y + halfHeight, halfWidth, halfHeight) // sw
			};
			
			for(int i = 0; i < 4; i++) {	
				if (bbs[i].intersect(object)) {
					if (divisions[i] == null) {
						divisions[i] = new QuadTree(this, bbs[i], depth+1, g, new Color(255, 0, 0));
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
		g.fillArc(root.dx + (int) object.x, root.dy + (int) object.y, (int) object.width, (int) object.height, 0, 360);
		
	}
	/** recursively calls itself to search for and populate a list reference with all objects within rect */
	private void get(ObjectBoundingBox rect, HashSet<ObjectBoundingBox> list, Graphics g) {		
		for(int i = 0; i < 4; i++) {
			if (divisions[i] != null) {
				if (rect.intersect(divisions[i].bb)) {
					divisions[i].container.forEach((e) -> {
						g.setColor(new Color(0, 0, 255));
						g.drawArc(root.dx + (int) e.x - 2, root.dy + (int) e.y - 2, (int) e.width + 4 , (int) e.height + 4, 0, 360);
						if (rect.intersect(e)) {
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
	public HashSet<ObjectBoundingBox> get(ObjectBoundingBox rect, Graphics g) {
		HashSet<ObjectBoundingBox> list = new HashSet<>();

		get(rect, list, g);
		
		g.setColor(new Color(255, 0, 255));
		g.drawRect(root.dx + (int) rect.x - 1, root.dy + (int) rect.y - 1, (int) rect.width + 2, (int) rect.height + 2);
		g.drawRect(root.dx + (int) rect.x, root.dy + (int) rect.y, (int) rect.width, (int) rect.height);
		return list;
	}
}