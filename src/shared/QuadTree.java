package shared;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;

public class QuadTree {
	private int maxDepth = 4;
	private int depth = 0;

	private ArrayList<BoundingBox> container = null;
	private QuadTree root = null;
	private QuadTree[] divisions = { null, null, null, null};
	
	private BoundingBox bb = null;
	
	public double x = 0;
	public double y = 0;
	public double width = 200;
	public double height = 200;
	
	// drawing
	public double dx = 0;
	public double dy = 0;
	
	public boolean divided = false;
	
	// debug with graphics
	public QuadTree(int maxDepth, int dx, int dy, int x, int y, int width, int height, ArrayList<BoundingBox> objects, Graphics g) {

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
	public QuadTree(QuadTree parent, BoundingBox bb, int depth, Graphics g, Color color) {

		if (parent == null) 
			return;
		
		this.root = parent.root;
		this.bb = bb;
		
		this.container = new ArrayList<>();
		this.maxDepth = root.maxDepth;
		this.depth = depth;
		
		this.x = bb.x;
		this.y = bb.y;
		this.width = bb.xscale;
		this.height = bb.yscale;
		
		this.dx = root.dx+x;
		this.dy = root.dy+y;
		
		this.divided = false;
		
		g.setColor(new Color(0, 0, 0));
		g.drawRect((int)dx, (int)dy, (int)width, (int)height);
		
	}
	private void insert(BoundingBox object, Graphics g) {
		//System.out.println(depth + " of " + maxDepth);
		if (depth < maxDepth) {
			double halfWidth = width/2;
			double halfHeight = height/2;

			// the order is important, quadtree goes clockwise starting at NE
			BoundingBox[] bbs = { 
					new BoundingBox(x, y, halfWidth, halfHeight), // ne
					new BoundingBox(x + halfWidth, y, halfWidth, halfHeight), // nw 
					new BoundingBox(x, y + halfHeight, halfWidth, halfHeight), // se
					new BoundingBox(x + halfWidth, y + halfHeight, halfWidth, halfHeight) // sw
			};
			
			for(int i = 0; i < 4; i++) {	
				if (bbs[i].intersect2d(object)) {
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
		g.fillArc((int) (root.dx + object.x), (int) (root.dy + object.y), (int) object.xscale, (int) object.yscale, 0, 360);
		
	}
	/** recursively calls itself to search for and populate a list reference with all objects within rect */
	private void get(BoundingBox rect, HashSet<BoundingBox> list, Graphics g) {		
		for(int i = 0; i < 4; i++) {
			if (divisions[i] != null) {
				if (rect.intersect2d(divisions[i].bb)) {
					divisions[i].container.forEach((e) -> {
						g.setColor(new Color(0, 0, 255));
						g.drawArc((int) (root.dx + e.x - 2), (int) (root.dy + e.y - 2), (int) (e.xscale + 4), (int) (e.yscale + 4), 0, 360);
						if (rect.intersect2d(e)) {
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
	public HashSet<BoundingBox> get(BoundingBox rect, Graphics g) {
		HashSet<BoundingBox> list = new HashSet<>();

		get(rect, list, g);
		
		g.setColor(new Color(255, 0, 255));
		g.drawRect((int) (root.dx + rect.x - 1), (int) (root.dy + rect.y - 1), (int) rect.xscale + 2, (int) rect.yscale + 2);
		g.drawRect((int) (root.dx + rect.x), (int) (root.dy + rect.y), (int) rect.xscale, (int) rect.yscale);
		return list;
	}
}