package shared;

/* this class is just a mirror of input states from the client */
public class InputController {
	
	public static InputMap MAP_LEFT 	= new InputMap(1);
	public static InputMap MAP_RIGHT 	= new InputMap(2);
	public static InputMap MAP_FORWARD 	= new InputMap(4);
	public static InputMap MAP_BACKWARD = new InputMap(8);
	public static InputMap MAP_FIRE 	= new InputMap(16);
	public static InputMap MAP_ALTFIRE 	= new InputMap(32);
	public static InputMap MAP_JUMP 	= new InputMap(64);
	public static InputMap MAP_ACTION 	= new InputMap(128);
	
}
