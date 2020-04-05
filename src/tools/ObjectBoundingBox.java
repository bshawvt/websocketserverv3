package tools;

public class ObjectBoundingBox {
	public double x, y, width, height;
	public ObjectBoundingBox(double[] pos, double[] bounds) {
		x = Math.floor(pos[0]);
		y = Math.floor(pos[1]);
		width = Math.floor(bounds[0]);
		height = Math.floor(bounds[1]);
	}
}
