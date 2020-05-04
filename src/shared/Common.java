package shared;

public class Common {
	static public int Clamp(int val, int min, int max) {
		if (val < min)
			val = min;
		else if (val > max)
			val = max;
		return val;
	}
	static public double Clamp(double val, double min, double max) {
		if (val < min)
			val = min;
		else if (val > max)
			val = max;
		return val;
	}
}
