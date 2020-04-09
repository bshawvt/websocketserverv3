package tools;

public class ObjectBoundingBox {
	public double x, y, width, height;
	public ObjectBoundingBox(double[] pos, double[] bounds) {
		x = Math.floor(pos[0]);
		y = Math.floor(pos[1]);
		width = Math.floor(bounds[0]);
		height = Math.floor(bounds[1]);
	}
	public ObjectBoundingBox(int[] pos, int[] bounds) {
		x = pos[0];
		y = pos[1];
		width = bounds[0];
		height = bounds[1];
	}
	public ObjectBoundingBox(int i, int j, int width, int height) {
		this.x = i;
		this.y = j;
		this.width = width;
		this.height = height;
	}
	public boolean intersect(ObjectBoundingBox bb) {
		double ax = x;
		double ay = y;
		double aw = x + width;
		double ah = y + height;
		
		double bx = bb.x;
		double by = bb.y;
		double bw = bb.x + bb.width;
		double bh = bb.y + bb.height;
	
		return ((ax <= bw && aw >= bx && ay <= bh && ah >= by));
		//return ((ax >= bw && aw <= bx && ay >= bh && ah <= by));
		
		//return (((x <= bb.x + bb.width) && (x+width >= bb.x)) &&
		//	((y <= bb.y + bb.height) && (y >= bb.y)));
	}
}
