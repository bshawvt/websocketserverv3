package tools;

public class ObjectBoundingBox {
	public double x, y, width, height;
	public boolean added = false;
	public ObjectBoundingBox() {
		x = 0;
		y = 0;
		width = 1;
		height = 1;
	}
	public ObjectBoundingBox(double[] pos, double[] bounds) {
		x = pos[0];
		y = pos[1];
		width = bounds[0];
		height = bounds[1];
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
	public ObjectBoundingBox(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public ObjectBoundingBox(double[] pos, double[] bounds, double radius) {
		double r2 = radius/2;
		x = pos[0] - r2;
		y = pos[1] - r2;
		width = bounds[0] + radius;
		height = bounds[1] + radius;
	}
	public void set(double[] pos, double[] bounds) {
		x = pos[0];
		y = pos[1];
		width = bounds[0];
		height = bounds[1];
	}
	public void set(int[] pos, int[] bounds) {
		x = pos[0];
		y = pos[1];
		width = bounds[0];
		height = bounds[1];
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
	}
}
