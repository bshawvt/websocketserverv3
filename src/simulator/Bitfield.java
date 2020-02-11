package simulator;

public class Bitfield {
	
	public int field;
		
	public Bitfield() {
			
	}
	public Bitfield(int bitfield) {
		this.field = bitfield;
	}
	/* sets bit to 1 */
	public void add(int bit) {
		this.field |= bit;
		
	}
	/* sets bit to 0 */
	public void subtract(int bit) {
		this.field &= ~bit;
	}
	/* returns bits */
	public int get() {
		return this.field;
	}
	/* returns 0 if the bit is unset */
	public int compare(int bit) {
		return this.field & bit;
	}
	/* overwrite bits  */
	public void set(int bitfield) {
		this.field = bitfield;
	}
}
	
	/*
	function Bitmask() {
		this.mask = 0;
	}
	Bitmask.prototype.add = function(x) {
		return this.mask |= x;
	}
	Bitmask.prototype.sub = function(x) {
		return this.mask &= ~x;
	}
	Bitmask.prototype.get = function() {
		return this.mask;
	}
	Bitmask.prototype.compare = function(x) {
		return this.mask & x;
	}
	Bitmask.prototype.clear = function() {
		this.mask = 0;
	}

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
	*/

	/* bits and masks

	[1 ]	bits |= 1
	[2 ]	bits |= 2
	[3 ]	bits |= 4
	[4 ]	bits |= 8
	[5 ]	bits |= 16
	[6 ]	bits |= 32
	[7 ]	bits |= 64
	[8 ]	bits |= 128
	[9 ]	bits |= 256
	[10]	bits |= 512
	[11]	bits |= 1024
	[12]	bits |= 2048
	[13]	bits |= 4096
	[14]	bits |= 8192
	[15]	bits |= 16384
	[16]	bits |= 32768
	[17]	bits |= 65536
	[18]	bits |= 131072
	[19]	bits |= 262144
	[20]	bits |= 524288
	[21]	bits |= 1048576
	[22]	bits |= 2097152
	[23]	bits |= 4194304
	[24]	bits |= 8388608
	[25]	bits |= 16777216
	[26]	bits |= 33554432
	[27]	bits |= 67108864
	[28]	bits |= 134217728
	[29]	bits |= 268435456
	[30]	bits |= 536870912
	[31]	bits |= 1073741824

	*/

	/* out of integer range
	= 2147483648
	= 4294967296

	*/
//}
