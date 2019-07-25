package simulator.entities;

import javax.swing.text.Position;

import simulator.World;
import util.Bitmask;
import util.L;

public class Avatar extends EntityObject {
	public Avatar() {
		super();
	}
	private long strafeTime = 0;
	private long accelerateTime = 0;
	private long lastStrafeTime = 0;
	private long lastAccelerateTime = 0;
	private int dashCount = 0;
	private boolean canDash = true;
	double tmp = 0.0;
	@Override
	public void step(long dt) {

		double[] pos = getPosition();
		double[] spd = getForwardSpeed();
		double[] rspd = getStrafeSpeed();

		double modifier = 0;
		int strafe = 0;
		if (getInputState().compare(Bitmask.INPUT_LEFT) == Bitmask.INPUT_LEFT) {
			strafe = 1;
			strafeTime = dt;
			//System.out.println(dt);
			/*if (canDash)
			if (strafeTime - lastStrafeTime < 200) {
				if (dashCount >= 2) {
					modifier += 0.1;
					dashCount = 0;
				}
				else {
					dashCount++;
				}
				lastStrafeTime = dt;
			}*/

		}
		if (getInputState().compare(Bitmask.INPUT_RIGHT) == Bitmask.INPUT_RIGHT) {
			strafe = 2;
			strafeTime = dt;
			/*if (canDash)
			if (accelerateTime - lastAccelerateTime < 200) {
				if (dashCount >= 2) {
					modifier += 0.1;
					dashCount = 0;
				}
				else {
					dashCount++;
				}
				lastAccelerateTime = dt;
			}*/
		}
		
		int accelerator = 0;
		if (getInputState().compare(Bitmask.INPUT_FORWARD) == Bitmask.INPUT_FORWARD) {
			accelerator = 1;
			accelerateTime = dt;
			/*if (canDash)
			if (accelerateTime - lastAccelerateTime < 1000) {
				if (dashCount > 2) {
					modifier += 0.1;
					dashCount = 0;
				}
				else {
					dashCount++;
				}
				lastAccelerateTime = dt;
			}*/
		}
		if (getInputState().compare(Bitmask.INPUT_BACK) == Bitmask.INPUT_BACK) {
			accelerator = 2;
			accelerateTime = dt;
			/*if (canDash)
			if (accelerateTime - lastAccelerateTime < 1000) {
				if (dashCount > 2) {
					modifier += 0.1;
					dashCount = 0;
				}
				else {
					dashCount++;
				}
				lastAccelerateTime = dt;
			}*/
		}

		
		

		double a1 = Math.sqrt((spd[0] * spd[0]) + 
								(spd[1] * spd[1]));/* +
								(spd[2] * spd[2]) );*/	
		a1 = (a1>=0.105 ? 0.105 : a1);
		a1 = Math.floor(a1 * 10000) / 10000;
		if (a1 > tmp) { tmp = a1; };
		//if (a1 < tmp) { System.out.println("stopped at " + pos[0] + ", " + pos[1]); };
	
	
		
		if (accelerator == 1) {
			//L.d("xx");
			//double modifier = 0;
			//if (dt > lastAccelerateTime + 1000)
			spd[0] += 0.005 + modifier;
			spd[1] += 0.005 + modifier;
		}
		else if (accelerator == 2) {
			//double modifier = 0;
			//if (dt > lastAccelerateTime + 1000)
			spd[0] -= 0.005;
			spd[1] -= 0.005;
		}
		else {
			if (spd[0] > 0.0001) spd[0] += -0.005;
			if (spd[1] > 0.0001) spd[1] += -0.005;
			
			if (spd[0] < 0.0001) spd[0] += 0.005;
			if (spd[1] < 0.0001) spd[1] += 0.005;
			if (a1 <= 0.009) {
				spd[0] = 0.0;
				spd[1] = 0.0;
			}
		}
		

		double a2 = Math.sqrt((rspd[0] * rspd[0]) + 
								(rspd[1] * rspd[1]));/* +
								(spd[2] * spd[2]) );*/	
		a2 = (a2>=0.105 ? 0.105 : a2);
		a2 = Math.floor(a2 * 10000) / 10000;
		if (strafe == 1) {
			//double modifier = 0;
			//if (dt > lastStrafeTime + 1000)
			
			rspd[0] += 0.005 + modifier;
			rspd[1] += 0.005 + modifier;
		}
		else if (strafe == 2) {
			//double modifier = 0;
			//if (dt > lastStrafeTime + 1000)
			
			rspd[0] -= 0.005 + modifier;
			rspd[1] -= 0.005 + modifier;
		}
		else {
			if (rspd[0] > 0.0001) rspd[0] += -0.005;
			if (rspd[1] > 0.0001) rspd[1] += -0.005;
			
			if (rspd[0] < 0.0001) rspd[0] += 0.005;
			if (rspd[1] < 0.0001) rspd[1] += 0.005;
			if (a2 <= 0.009) {
				rspd[0] = 0.0;
				rspd[1] = 0.0;
			}
		}
		
	
		spd[0] -= 0.5 * spd[0] * a1;
		spd[1] -= 0.5 * spd[1] * a1;
		
		rspd[0] -= 0.5 * rspd[0] * a2;
		rspd[1] -= 0.5 * rspd[1] * a2;
		// old bad
		
		double a = (Math.PI/180) * getForwardAngle();
		//System.out.println(getForwardAngle());
		double r = (Math.PI/180) * 90;
		//Math.PI/180 * getForwardAngle()
		
		/*if (strafe == 1) {
			rspd[0] = 0.05;
			rspd[1] = 0.05;
		}
		else if (strafe == 2) {
			rspd[0] = -0.05;
			rspd[1] = -0.05;
		}
		else {
			rspd[0] = 0.0;
			rspd[1] = 0.0;
		}

		if (accelerator == 1) {
			spd[0] = 0.05;
			spd[1] = 0.05;
		}
		else if (accelerator == 2) {
			spd[0] = -0.05;
			spd[1] = -0.05;
		}
		else {
			spd[0] = 0.0;
			spd[1] = 0.0;
		}*/

		pos[0] += Math.sin(a - r) * rspd[0];
		pos[1] -= Math.cos(a - r) * rspd[1];
		
		
		pos[0] -= Math.sin(a) * spd[0];
		pos[1] += Math.cos(a) * spd[1];

		setPosition(pos);
		setForwardSpeed(spd);
		setStrafeSpeed(rspd);
		
		//setHealth(2147483647);
		//System.out.println(pos[1]);
	}
	
	/*
	 * 
	 * //Gravity
spd[1] += 9.807;
//Aerodynamic friction
absSpd = sqrt(spd[0]*spd[0] + spd[1]*spd[1]);
spd[0] -= 0.001*spd[0]*absSpd;
spd[1] -= 0.001*spd[1]*absSpd;

pos[0] += spd[0];
pos[1] += spd[1];
	 */

}
