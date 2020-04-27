package shared;

public class BoundingBox {
	public double x, y, z, xscale, yscale, zscale;
	public BoundingBox() {
		x = 0;
		y = 0;
		z = 0;
		xscale = 1;
		yscale = 1;
	}
	public BoundingBox(double[] pos, double[] bounds) {
		x = pos[0];
		y = pos[1];
		z = pos[2];
		xscale = bounds[0];
		yscale = bounds[1];
	}
	public BoundingBox(int[] pos, int[] bounds) {
		x = pos[0];
		y = pos[1];
		xscale = bounds[0];
		yscale = bounds[1];
	}
	public BoundingBox(int i, int j, int width, int height) {
		this.x = i;
		this.y = j;
		this.xscale = width;
		this.yscale = height;
	}
	public BoundingBox(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.xscale = width;
		this.yscale = height;
	}
	
	public BoundingBox(double[] pos, double[] bounds, double radius) {
		double r2 = radius/2;
		x = pos[0] - r2;
		y = pos[1] - r2;
		xscale = bounds[0] + radius;
		yscale = bounds[1] + radius;
	}
	public void set(double[] pos, double[] bounds) {
		x = pos[0];
		y = pos[1];
		z = pos[2];
		xscale = bounds[0];
		yscale = bounds[1];
	}
	public void set(int[] pos, int[] bounds) {
		x = pos[0];
		y = pos[1];
		xscale = bounds[0];
		yscale = bounds[1];
	}
	
	public boolean intersect2d(BoundingBox bb) {
		double ax = x;
		double ay = y;
		double aw = x + xscale;
		double ah = y + yscale;
		
		double bx = bb.x;
		double by = bb.y;
		double bw = bb.x + bb.xscale;
		double bh = bb.y + bb.yscale;
	
		return ((ax <= bw && aw >= bx && ay <= bh && ah >= by));
	}
	public boolean intersect3d(BoundingBox bb) {
		double ax = x;
		double ay = y;
		double az = z;
		double aw = x + xscale;
		double ah = y + yscale;
		double ad = y + yscale;
		
		double bx = bb.x;
		double by = bb.y;
		double bz = bb.z;
		double bw = bb.x + bb.xscale;
		double bh = bb.y + bb.yscale;
		double bd = bb.z + bb.zscale;
	
		return (ax <= bw && aw >= bx && 
				ay <= bh && ah >= by &&
				az <= bd && ad >= bz);
	}
}
