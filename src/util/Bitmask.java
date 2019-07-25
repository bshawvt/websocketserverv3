package util;

public class Bitmask {
	private int mask = 0;
	public int getMask() { return mask; };
	public void setMask(int v) { this.mask = v; };
	
	
	public Bitmask() {
		mask = 0;
	}
	
	public int add(int v) { return mask |= v; };
	public int sub(int v) { return mask &= ~v; };
	public int compare(int v) { return mask & v; };
	public void clear() { mask = 0; };
	
	public void pushState(int state, boolean active) { if (active)  { this.add(state); } else { this.sub(state); } };
	public void setState(int state) { this.mask = state; };

	public static final int INPUT_LEFT = 2;
	public static final int INPUT_RIGHT = 4;
	public static final int INPUT_FORWARD = 8;
	public static final int INPUT_BACK = 16;
	public static final int INPUT_SPACE = 32;
	
	public static final int STATE_JUMP = 2;
	public static final int STATE_FALLING = 4;
	public static final int STATE_EXHAUSTED = 8;
	
	/*
		= 256
		= 512
		= 1024
		= 2048
		= 4096
		= 8192
		= 16384
		= 32768
		= 65536
		= 131072
		= 262144
		= 524288
		= 1048576
		= 2097152
		= 4194304
		= 8388608
		= 16777216
		= 33554432
		= 67108864
		= 134217728
		= 268435456
		= 536870912
		= 1073741824
		= 2147483648
		= 4294967296
	 */
		
}

/*
=

Bitmask.prototype.pushState = function(state) {
this.mask = state;
};
Bitmask.prototype.setState = function(state, active) {
if (active === true) {
	this.add(state);
}
else {
	this.sub(state);
}
};

STATE_DOWN_LEFT = 2;
STATE_DOWN_RIGHT = 4;
STATE_DOWN_FORWARD = 8;
STATE_DOWN_BACK = 16;
*/