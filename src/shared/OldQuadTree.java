package shared;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;

import simulator.sceneobjects.SceneObject;

public class OldQuadTree {
	
	
	private int levels = 4;
	private int level = 0;

	private ArrayList<BoundingBox> container = null;
	private OldQuadTree root = null;
	private OldQuadTree parent = null;
	private OldQuadTree ne = null;
	private OldQuadTree nw = null;
	private OldQuadTree se = null;
	private OldQuadTree sw = null;
	
	private OldQuadTree[] divisions = { null, null, null, null};
	
	private String name = null;
	
	private BoundingBox bb = null;
	
	public int x = 0;
	public int y = 0;
	public int width = 200;
	public int height = 200;
	
	public int dx = 0;
	public int dy = 0;
	public int dwidth = 200;
	public int dheight = 200;
	
	public boolean divided = false;
	
	// debug with graphics
	public OldQuadTree(int dx, int dy, int x, int y, int width, int height, ArrayList<BoundingBox> objects, Graphics g) {

		this.root = this;
		this.container = new ArrayList<>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.dx = dx;
		this.dy = dy;
		this.dwidth = width;
		this.dheight = height;
		
		this.divided = false;
		
		this.bb = new BoundingBox(x, y, width, height);
		
		this.name = "root";
		
		g.drawRect(dx, dy, width, height);
		//subdivide(g, 0);
		//insert(objects);
		objects.forEach((e) -> {
			insert(e, g);
		});
		

	}
	
	/** this constructor should never be used to create the quadtree */
	public OldQuadTree(OldQuadTree parent, BoundingBox bb, int level, Graphics g, Color color, String name) {
			
		//System.out.println("made quad for " + name);
		if (parent == null) 
			return;
		
		this.root = parent.root;
		
		this.bb = bb;
		
		this.container = new ArrayList<>();
		this.name = name;
		this.level = level;
		
		this.x = (int) Math.floor(bb.x);
		this.y = (int) Math.floor(bb.y);
		this.width = (int) Math.floor(bb.xscale); //parent.width / 2;
		this.height = (int) Math.floor(bb.yscale); //parent.height / 2;
		
		this.dx = root.dx+x;
		this.dy = root.dy+y;
		this.dwidth = width;
		this.dheight = height;
		
		this.divided = false;
		
		//this.currentLevel = parent.currentLevel + 1;
		
		
		g.setColor(new Color(0, 0, 0));
		//g.setColor(new Color((int) Math.floor(Math.random() * (255 * 255 * 255))));
		
		g.drawRect(dx, dy, width, height);
		//g.setColor(color);
		//g.fillRect(x, y, width, height);
		//insert(objects);
		
	}
	private void insert(BoundingBox object, Graphics g) {
		System.out.println(level + " of " + levels);
		if (level < levels) {
			int halfWidth = width/2;
			int halfHeight = height/2;
								
			boolean added = false;
			// ne
			BoundingBox neBB = new BoundingBox(x, y, halfWidth, halfHeight);
			BoundingBox nwBB = new BoundingBox(x + halfWidth, y, halfWidth, halfHeight);
			BoundingBox seBB = new BoundingBox(x, y + halfHeight, halfWidth, halfHeight);
			BoundingBox swBB = new BoundingBox(x + halfWidth, y + halfHeight, halfWidth, halfHeight);
			
			BoundingBox[] bbs = { neBB, nwBB, seBB, swBB};
			String[] names = { "ne", "nw", "se", "sw" };
			
			for(int i = 0; i < 4; i++) {
				
				if (leafContains(bbs[i], object)) {
					if (divisions[i] == null) {
						divisions[i] = new OldQuadTree(this, bbs[i], level+1, g, new Color(255, 0, 0), names[i]);
					}
					divisions[i].divided = true;
					divisions[i].insert(object, g);
					added = true;
				}
				
			}
			/*
			// nw
			if (leafContains(neBB, object)) {
				if (ne == null) {
					ne = new QuadTree(this, neBB, level+1, g, new Color(255, 0, 0), "ne");
				}
				ne.divided = true;
				ne.insert(object, g);
				added = true;
			}
			
			// nw
			
			if (leafContains(nwBB, object)) {
				if (nw == null) {		
					nw = new QuadTree(this, nwBB, level+1, g, new Color(255, 0, 0), "nw");
				}
				nw.divided = true;
				nw.insert(object, g);
				added = true;
			}
			
			// se
			
			if (leafContains(seBB, object)) {
				if (se == null) {		
					se = new QuadTree(this, seBB, level+1, g, new Color(255, 0, 0), "se");
				}
				se.divided = true;
				se.insert(object, g);
				added = true;
			}
			
			// sw
			
			if (leafContains(swBB, object)) {
				if (sw == null) {		
					sw = new QuadTree(this, swBB, level+1, g, new Color(255, 0, 0), "sw");
				}
				sw.divided = true;
				sw.insert(object, g);
				added = true;
			}*/
			System.err.println("was added is " + added);
		}
		else {
			add(object);
		}
		
		g.setColor(new Color(255, 0, 0));
		g.fillArc(root.dx + (int) object.x, root.dy + (int) object.y, (int) object.xscale, (int) object.yscale, 0, 360);
		
	}
	private boolean leafContains(BoundingBox leafBB, BoundingBox objectBB) {
		if (leafBB.intersect2d(objectBB)) {
			return true;
		}
		return false;
	}
	/** adds an object to a leaf */
	private void add(BoundingBox object) {
		System.err.println("added thing");
		
		container.add(object);
	}

	private void addSearchItem(BoundingBox rect, OldQuadTree leaf, HashSet<BoundingBox> list, Graphics g) {
		leaf.container.forEach((e) -> {
			g.setColor(new Color(0, 0, 255));
			g.drawArc(root.dx + (int) e.x - 2, root.dy + (int) e.y - 2, (int) e.xscale + 4 , (int) e.yscale + 4, 0, 360);
			if (rect.intersect2d(e)) {
				list.add(e);
				
			}
		});
	}
	
	/** recursively calls itself to search for and populate a list reference with all objects within rect */
	//private void get(ObjectBoundingBox rect, ArrayList<ObjectBoundingBox> list, Graphics g) {
	private void get(BoundingBox rect, HashSet<BoundingBox> list, Graphics g) {		
		
		for(int i = 0; i < 4; i++) {
			if (divisions[i] != null) {
				if (rect.intersect2d(divisions[i].bb)) {
					addSearchItem(rect, divisions[i], list, g);
					if (divisions[i].divided) {
						divisions[i].get(rect, list, g);
					}
				}
			}
		}
		
		/*
		if (ne != null) {
			if (rect.intersect(ne.bb)) {
				addSearchItem(rect, ne, list, g);
				if (ne.divided) {
					ne.get(rect, list, g);
				}
			}
		}
		if (nw != null) {
			if (rect.intersect(nw.bb)) {
				addSearchItem(rect, nw, list, g);
				if (nw.divided) {
					nw.get(rect, list, g);
				}
			}
		}
		if (se != null) {
			if (rect.intersect(se.bb)) {
				addSearchItem(rect, se, list, g);
				if (se.divided) {
					se.get(rect, list, g);
				}
			}
		}
		if (sw != null) {
			if (rect.intersect(sw.bb)) {
				addSearchItem(rect, sw, list, g);
				if (sw.divided) {
					sw.get(rect, list, g);
				}
			}
		}*/

	}
	/*private void get(ObjectBoundingBox rect, HashSet<ObjectBoundingBox> list, Graphics g) {
		
		HashSet<ObjectBoundingBox> set = new HashSet<>();
		
		if (ne != null) {
			if (rect.intersect(ne.bb)) {
				ne.container.forEach((e) -> {
					g.setColor(new Color(0, 0, 255));
					g.drawArc(root.dx + (int) e.x - 2, root.dy + (int) e.y - 2, (int) e.width + 4 , (int) e.height + 4, 0, 360);
					if (rect.intersect(e)&& !e.added) {
						e.added = true;
						list.add(e);
						
					}
				});
				if (ne.divided) {
					ne.get(rect, list, g);
				}
			}
		}
		if (nw != null) {
			if (rect.intersect(nw.bb)) {
				nw.container.forEach((e) -> {
					g.setColor(new Color(0, 0, 255));
					g.drawArc(root.dx + (int) e.x - 2, root.dy + (int) e.y - 2, (int) e.width + 4 , (int) e.height + 4, 0, 360);
					if (rect.intersect(e) && !e.added) {
						e.added = true;
						list.add(e);
					}
				});
				if (nw.divided) {
					nw.get(rect, list, g);
				}
			}
		}
		if (se != null) {
			if (rect.intersect(se.bb)) {
				se.container.forEach((e) -> {
					g.setColor(new Color(0, 0, 255));
					g.drawArc(root.dx + (int) e.x - 2, root.dy + (int) e.y - 2, (int) e.width + 4 , (int) e.height + 4, 0, 360);
					if (rect.intersect(e) && !e.added) {
						e.added = true;
						list.add(e);
					}
				});
				if (se.divided) {
					se.get(rect, list, g);
				}
			}
		}
		if (sw != null) {
			if (rect.intersect(sw.bb)) {
				sw.container.forEach((e) -> {
					g.setColor(new Color(0, 0, 255));
					g.drawArc(root.dx + (int) e.x - 2, root.dy + (int) e.y - 2, (int) e.width + 4 , (int) e.height + 4, 0, 360);
					if (rect.intersect(e) && !e.added) {
						e.added = true;
						list.add(e);
					}
				});
				if (sw.divided) {
					sw.get(rect, list, g);
				}
			}
		}

	}*/
	/**
	 * 
	 * @param rect
	 * @param g
	 * @return arraylist of objects within rect
	 */
	//public ArrayList<ObjectBoundingBox> get(ObjectBoundingBox rect, Graphics g) {
	public HashSet<BoundingBox> get(BoundingBox rect, Graphics g) {
		//ArrayList<ObjectBoundingBox> list = new ArrayList<>();
		HashSet<BoundingBox> list = new HashSet<>();
		
		
		get(rect, list, g);
		
		g.setColor(new Color(255, 0, 255));
		g.drawRect(root.dx + (int) rect.x - 1, root.dy + (int) rect.y - 1, (int) rect.xscale + 2, (int) rect.yscale + 2);
		g.drawRect(root.dx + (int) rect.x, root.dy + (int) rect.y, (int) rect.xscale, (int) rect.yscale);
		return list;
	}
}
