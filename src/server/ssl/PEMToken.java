package server.ssl;

import java.util.Base64;

public abstract class PEMToken {
	 	public abstract byte[] get(String token);
}
