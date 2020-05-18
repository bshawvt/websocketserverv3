package shared;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.SerializedName;

import simulator.WorldLoader.WorldLoaderObject.WorldSceneObject;
import simulator.WorldLoader.WorldLoaderObject.WorldSceneObject.WorldSceneObjectArgs;
import simulator.sceneobjects.SceneObject;
import simulator.sceneobjects.SceneTile;

import tools.Profiler;
import tools.Profiler.*;


public class SOQuadTree {
	private int maxDepth = 4;
	private int depth = 0;
	
	public int accumulator = 0;

	private ObjectList<SceneObject> container = null;
	private SOQuadTree root = null;
	private SOQuadTree[] divisions = { null, null, null, null};
	
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
	public SOQuadTree(int maxDepth, int dx, int dy, int x, int y, int width, int height, ArrayList<SceneObject> objects, Graphics g) {

		this.root = this;
		this.container = new ObjectList<>();
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
	public SOQuadTree(SOQuadTree parent, BoundingBox bb, int depth, Graphics g, Color color) {

		if (parent == null) 
			return;
		
		this.root = parent.root;
		this.bb = bb;
		
		this.container = new ObjectList<>();
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
	private void insert(SceneObject object, Graphics g) {
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
				//if (bbs[i].intersect2d(new BoundingBox(object.position, object.bounds))) {
				if (bbs[i].intersect2d(object.bb)) {
					if (divisions[i] == null) {
						divisions[i] = new SOQuadTree(this, bbs[i], depth+1, g, new Color(255, 0, 0));
					}
					divisions[i].divided = true;
					divisions[i].insert(object, g);
				}
			}
		}
		else {
			container.add(object);
		}
		
		//g.setColor(new Color(255, 0, 0));
		//g.fillArc(root.dx + (int) object.x, root.dy + (int) object.y, (int) object.bb.xscale, (int) object.bb.yscale, 0, 360);
		
	}
	/** recursively calls itself to search for and populate a list reference with all objects within rect */
	private void get(BoundingBox rect, ObjectList<SceneObject> list, Graphics g) {
		
		for(int i = 0; i < 4; i++) {
			if (divisions[i] != null) {
				if (rect.intersect2d(divisions[i].bb)) {
					divisions[i].container.forEach((e) -> {
					
						g.setColor(new Color(0, 0, 255));
						g.drawArc(root.dx + (int) e.x - 2, root.dy + (int) e.y - 2, (int) e.bb.xscale + 4 , (int) e.bb.yscale + 4, 0, 360);
						if (rect.intersect2d(e.bb)) {
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
	public ObjectList<SceneObject> get(BoundingBox rect, Graphics g) {
		ObjectList<SceneObject> list = new ObjectList<>();

		get(rect, list, g);
		
		g.setColor(new Color(255, 0, 255));
		g.drawRect(root.dx + (int) rect.x - 1, root.dy + (int) rect.y - 1, (int) rect.xscale + 2, (int) rect.yscale + 2);
		g.drawRect(root.dx + (int) rect.x, root.dy + (int) rect.y, (int) rect.xscale, (int) rect.yscale);
		return list;
	}
	
	
	// no graphics
	public SOQuadTree(int maxDepth, int x, int y, int width, int height, ObjectList<SceneObject> objects) {

		this.root = this;
		this.container = new ObjectList<>();
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
	public SOQuadTree(SOQuadTree parent, BoundingBox bb, int depth) {

		if (parent == null) 
			return;
		
		this.root = parent.root;
		this.bb = bb;
		
		this.container = new ObjectList<>();
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
	private void insert(SceneObject object) {
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
			int bigBoyCount = 0;
			int quad = 0;
			for(int i = 0; i < 4; i++) {	
				if (bbs[i].intersect2d(object.bb)) {
					bigBoyCount++;
					quad = i;
					/*if (divisions[i] == null) {
						divisions[i] = new SOQuadTree(this, bbs[i], depth+1);
					}
					divisions[i].divided = true;
					divisions[i].insert(object);*/
				}
			}
			
			if (bigBoyCount == 1) {
				if (this.divisions[quad] == null) {
					divisions[quad] = new SOQuadTree(this, bbs[quad], depth+1);
				}
				divisions[quad].divided = true;
				divisions[quad].insert(object);
			}
			else {
				container.add(object);
			}
		}
		else {
			container.add(object);
		}

	}

	/** recursively calls itself to search for and populate a list reference with all objects within rect */
	private ObjectList<SceneObject> get(BoundingBox rect, ObjectList<SceneObject> list) {
		int c = 0;
		for(int i = 0; i < 4; i++) {
			if (divisions[i] != null) {
				if (rect.intersect2d(divisions[i].bb)) {
					list.link(divisions[i].container);
					if (divisions[i].divided) {
						divisions[i].get(rect, list);
					}
				}
			}
		}
		return list;
	}
	/**
	 * 
	 * @param rect
	 * @param g
	 * @return arraylist of objects within rect
	 */
	public ObjectList<SceneObject> get(BoundingBox rect) {
		ObjectList<SceneObject> list = new ObjectList<>();

		get(rect, list);
		
		return list;
	}
	
	public static void main(String[] args) {
		
		
		
		ObjectList<SceneObject> objs = new ObjectList<>();
		int objCount = 50000;
		int width = 200;
		int height = 200;
		for(int i = 0; i < objCount; i++) {
			objs.add(new SceneTile(width, height));
		}
		Profiler profiler = new Profiler();
		int b = 0;
		profiler.start("quadtree");
		SOQuadTree tree = new SOQuadTree(2, 0, 0, width, height, objs);
		ObjectList<SceneObject> set = null;
		BoundingBox bb = new BoundingBox(5, 5, 0, 50, 50, 0);
		
		System.out.println(tree.get(bb).length);
		
		for(int i = 0; i < objCount; i++) {
			
			set = tree.get(bb);
			set.forEach((e) -> {
				if (e.bb.intersect2d(bb)) {
					//System.err.println(e.clientId);
				}
			});
			
			/*set.forEach((e) -> {
				System.out.println(e.clientId);
			});*/
			/*for(SceneObject obj : set) {
				if (obj.bb.intersect2d(bb)) {
					b++;
				}
			}*/
		}
		
		
		profiler.stop("quadtree");
		profiler.print("quadtree");
		/*Profiler profiler2 = new Profiler();
		profiler2.start("ovo");
		int z = 0;
		for(SceneObject obj : objs) {
			for(SceneObject obj2 : objs) {
				if (obj.bb.intersect2d(obj2.bb)) {
					z++;
				}
			}
		}
		profiler2.stop("ovo");
		profiler2.print("ovo");*/
		
		
	}
	
}